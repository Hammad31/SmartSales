package com.app.sample.shop.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CatalogAdapter;
import com.app.sample.shop.adapter.OrderAdapter;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Order_Products;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    TextView xx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orders = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_order, null);
        global = (GlobalVariable) getActivity().getApplication();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        OrderProduct_RecyclerView = (RecyclerView) view.findViewById(R.id.Order_recyclerView);
        OrderProduct_RecyclerView.setLayoutManager(layoutManager);
        OrderProduct_RecyclerView.setHasFixedSize(true);
        OrderProduct_RecyclerView.setItemAnimator(new DefaultItemAnimator());

        /// just for test remove it after API implement
        Product p = new Product("chg",3,"rr",33,33,33,3,3,"mm","k","ll",3,3,"gg");
        Product p2 = new Product("hcj",43,"gfxh",33,33,33,3,3,"fgxh","xfgh","gf",3,3,"fgh");
        Product p3 = new Product("dhjkld",31,"hjkl",1,1,1,1,1,"lkh","klj","jhkl",1,1,"kjl");
        Order_Products order_products=new Order_Products(1,1,2,p);
        Order_Products order_products2=new Order_Products(1,1,2,p2);
        Order_Products order_products3=new Order_Products(1,1,2,p3);

         ArrayList<Order_Products> orderProductses = new ArrayList<>();
        orderProductses.add(order_products);
        orderProductses.add(order_products2);
        orderProductses.add(order_products3);

        Order x= new Order(2,"dd","ddd",22,"dff",54,4324,343,434,orderProductses);
        Order x2= new Order(2,"dd","ddd",22,"dff",54,4324,343,434,orderProductses);
        orders.add(x);
        orders.add(x2);
        ///

        mAdapter = new OrderAdapter(getActivity(), orders);
        OrderProduct_RecyclerView.setAdapter(mAdapter);
//        new Connect().execute();
        return view;
    }

    // API


}
