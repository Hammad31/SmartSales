package com.app.sample.shop.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sample.shop.ActivityMain;
import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.adapter.CatalogAdapter;
import com.app.sample.shop.adapter.ProductGridAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hammad on 12/01/16.
 */
public class Home_Page_Fragment extends android.support.v4.app.Fragment {
    private View view;
    private RecyclerView bestProduct_RecyclerView;
    private GlobalVariable global;
    private CatalogAdapter mAdapter;
    private ArrayList<Catalog> catalog;
    TextView xx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        catalog = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_home_page, null);
        global = (GlobalVariable) getActivity().getApplication();

//         xx = (TextView) view.findViewById(R.id.textViewmm);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        bestProduct_RecyclerView = (RecyclerView) view.findViewById(R.id.best_product_recyclerView);
        bestProduct_RecyclerView.setLayoutManager(layoutManager);
        bestProduct_RecyclerView.setHasFixedSize(true);
        bestProduct_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        bestProduct_RecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        //set data and list adapter
        // her we will use new adapter and we will use begone adapter inside it
        mAdapter = new CatalogAdapter(getActivity(), catalog);
        bestProduct_RecyclerView.setAdapter(mAdapter);
        new Connect().execute();
        return view;
    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        @Override
        protected Void doInBackground(Void... urls) {
            System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try{
//                URL url = new URL ("http://192.168.1.119:8080/Project/getCatalogWithProduct");
                URL url = new URL ("http://hamoha.com/Project/getCatalogWithProduct");
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
                JSONArray parentArray = parentObject.getJSONArray("catalogs");

                for(int i=0;i<parentArray.length();i++) {
                    ArrayList<Product> ProductList = new ArrayList<>();
                    JSONObject childObject = parentArray.getJSONObject(i);
                    String CatalogID = childObject.getString("CatalogID");
                    String Cname = childObject.getString("Cname");
                    JSONArray ProductArray = childObject.getJSONArray("products");

                    for(int v=0;v<ProductArray.length();v++) {
                        JSONObject JSONPro = ProductArray.getJSONObject(v);
                        String name = JSONPro.getString("name");
                        int quantity = JSONPro.getInt("quantity");
                        int PID = JSONPro.getInt("PID");
                        int CUID = JSONPro.getInt("CUID");
                        int ComID = JSONPro.getInt("ComID");
                        int CATALOGCatalogID = JSONPro.getInt("CATALOGCatalogID");
                        String date = JSONPro.getString("date");
                        String type = JSONPro.getString("type");
                        int price = JSONPro.getInt("price");
                        int like = JSONPro.getInt("like");
                        int sales = JSONPro.getInt("sales");
                        String info = JSONPro.getString("info");
                        String photo = JSONPro.getString("link");
                        String properties = JSONPro.getString("properties");

                        ProductList.add(new Product(photo,price, date, CUID, CATALOGCatalogID, ComID,PID ,quantity,info,type,name,like,sales,properties));
                    }
                    catalog.add(new Catalog(CatalogID,Cname,ProductList));
                }
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
            bestProduct_RecyclerView.invalidate();
//            xx.setText(catalog.get(0).getProduct().get(0).getName());
        }
    }
}
