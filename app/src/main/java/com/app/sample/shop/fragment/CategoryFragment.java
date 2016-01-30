package com.app.sample.shop.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.sample.shop.R;
import com.app.sample.shop.adapter.ItemGridAdapter;
import com.app.sample.shop.adapter.ProductGridAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.ItemModel;
import com.app.sample.shop.model.Product;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    public static String TAG_CATEGORY = "com.app.sample.shop.tagCategory";

    private View view;
    private RecyclerView recyclerView;
    private ItemGridAdapter mAdapter;
    private ProductGridAdapter yAdapter;
    private LinearLayout lyt_notfound;
    private String category = "";
    private ArrayList<Product> items;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, null);
        //category = getArguments().getString(TAG_CATEGORY);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lyt_notfound = (LinearLayout) view.findViewById(R.id.lyt_notfound);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //set data and list adapter
        items = new ArrayList<>();
        ///  nList<ItemModel> items = new ArrayList<>();
            new Connect().execute();
        /*    items = Constant.getItemClothes(getActivity());
        }
        else if (category.equals(getString(R.string.menu_shoes))) {
            items = Constant.getItemShoes(getActivity());
        } else if (category.equals(getString(R.string.menu_watches))) {
            items = Constant.getItemWatches(getActivity());
        } else if (category.equals(getString(R.string.menu_accessories))) {
            items = Constant.getItemAccessories(getActivity());
        } else if (category.equals(getString(R.string.menu_bags))) {
            items = Constant.getItemBags(getActivity());
        } else if (category.equals(getString(R.string.menu_new))) {
            items = Constant.getItemNew(getActivity());
        }
*/

        yAdapter = new ProductGridAdapter(getActivity(), items);
//        mAdapter = new ItemGridAdapter(getActivity(), items);
//        recyclerView.setAdapter(mAdapter);
        recyclerView.setAdapter(yAdapter);
        if (yAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }
        return view;
    }
    private class Connect extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... urls) {

            try {
                final String url = "jdbc:mysql://46.101.93.95:3306/SmartSales";
                final String user = "user1";
                final String pass = "password";
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Hammad: " + "Good");

                Statement st = con.createStatement();
                final ResultSet rs = st.executeQuery("select product.PID, name, price, type, photo, comName from product, photo, company where product.comID=company.comID and product.PID = photo.PID ");
                ResultSetMetaData rsmd = rs.getMetaData();

                int count = 1;
                while (rs.next()) {
                    Product product = new Product();
                    try {
                        product.setCompany(rs.getString("comName"));
                        product.setName(rs.getString("name"));
                        product.setPrice(rs.getInt("price"));
                        product.setType(rs.getString("type"));
                        product.setImage(rs.getBlob("photo"));
                        items.add(product);
                        System.out.println("Product: " + product.getName() + ", Price: " + product.getPrice() + ", Company" + product.getCompany());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Count: " + count);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
//                tvv.setText(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            yAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
        }


    }


}
