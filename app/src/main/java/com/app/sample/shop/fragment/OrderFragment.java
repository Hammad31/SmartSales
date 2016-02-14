package com.app.sample.shop.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CatalogAdapter;
import com.app.sample.shop.adapter.OrderAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Order_Products;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MOHAMMAD on 1/30/2016.
 */
public class OrderFragment extends Fragment {

    private View view;
    private RecyclerView OrderProduct_RecyclerView;
    private GlobalVariable global;
    private OrderAdapter mAdapter;
    private ArrayList<Order> orders;
    TextView xx2;
    ImageView img;
    boolean noItem=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orders = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_order, null);
        global = (GlobalVariable) getActivity().getApplication();

        xx2= (TextView) view.findViewById(R.id.textView11);
        img= (ImageView) view.findViewById(R.id.imageView);
        img.setVisibility(view.GONE);
        xx2.setVisibility(view.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        OrderProduct_RecyclerView = (RecyclerView) view.findViewById(R.id.Order_recyclerView);
        OrderProduct_RecyclerView.setLayoutManager(layoutManager);
        OrderProduct_RecyclerView.setHasFixedSize(true);
        OrderProduct_RecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new OrderAdapter(getActivity(), orders);
        OrderProduct_RecyclerView.setAdapter(mAdapter);
        new Connect().execute();
        return view;
    }


    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        SessionManager sessionManager= new SessionManager(getContext());
        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try{
                URL url = new URL ("http://hamoha.com/Project/getOrders?CID="+ sessionManager.getCurrentCustomerID());
//                URL url = new URL ("http://hamoha.com/Project/getOrders");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line="";
                while((line=reader.readLine())!= null){
                    buffer.append(line);
                }



                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = parentObject.getJSONArray("Orders");

                if(parentArray.length()<1) {
                    noItem=true;
                } else {
                    for(int i=0;i<parentArray.length();i++) {
                    ArrayList<Order_Products> orderProductsList = new ArrayList<>();
                    JSONObject childObject = parentArray.getJSONObject(i);
                    int OrderID=childObject.getInt("OID");
                    String dateOfOrder = childObject.getString("dateOfOrder");
                    String status=childObject.getString("status");
                    int shippingCost=childObject.getInt("shippingCost");
                    String deliverDate=childObject.getString("deliverDate");
                    int TotalCost=childObject.getInt("cost");
                    int AdderessID=childObject.getInt("AID");
                    int PaymentID=childObject.getInt("PID");
                    int ShipperSID=childObject.getInt("ShipperSID");
                    int CustomerID=childObject.getInt("customerID");
                    JSONArray orderProductsArray = childObject.getJSONArray("OrderProducts");

                    for(int v=0;v<orderProductsArray.length();v++) {
                        JSONObject JSONSingleOrderProduct = orderProductsArray.getJSONObject(v);
                        int OrderIDForSingleOrderProduct=JSONSingleOrderProduct.getInt("ORDEROID");
                        int ProductID=JSONSingleOrderProduct.getInt("PID");
                        int quantityForJSONSingleOrderProduct=JSONSingleOrderProduct.getInt("quantity");
                        // here get product
                        JSONObject Product = JSONSingleOrderProduct.getJSONObject("Product");
                        String name = Product.getString("name");
                        int quantity = Product.getInt("quantity");
                        int PID = Product.getInt("PID");
                        int CUID = Product.getInt("CUID");
                        int ComID = Product.getInt("ComID");
                        int CATALOGCatalogID = Product.getInt("CATALOGCatalogID");
                        String date = Product.getString("date");
                        String type = Product.getString("type");
                        int price = Product.getInt("price");
                        int like = 0;
                        int sales = 0;
                        String info = Product.getString("info");
                        String photo = Product.getString("link");
                        String properties = null;
                        orderProductsList.add(new Order_Products(OrderIDForSingleOrderProduct,ProductID,quantityForJSONSingleOrderProduct,new Product(photo,price, date, CUID, CATALOGCatalogID, ComID,PID ,quantity,info,type,name,like,sales,properties)));
                    }
                    orders.add(new Order(OrderID,CustomerID,dateOfOrder,status,shippingCost,deliverDate,TotalCost,AdderessID,PaymentID,ShipperSID,orderProductsList));
                }}
            }catch (Exception e){

            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
            OrderProduct_RecyclerView.invalidate();
            if(noItem){
                noItem=false;
                xx2.setVisibility(View.VISIBLE);
                xx2.setText("There Is No Order !!");
                img.setVisibility(View.VISIBLE);

            }
        }
    }

    // API


}
