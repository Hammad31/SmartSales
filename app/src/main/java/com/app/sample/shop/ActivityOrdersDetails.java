package com.app.sample.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.sample.shop.adapter.OrderAdapter;
import com.app.sample.shop.adapter.OrderProductsAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by MOHAMMAD on 1/31/2016.
 */
public class ActivityOrdersDetails extends AppCompatActivity {

    private RecyclerView OrderProduct_RecyclerView;
    private OrderProductsAdapter mAdapter;
    private Order orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_orders_details);

        orders = Constant.currentOrder;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        OrderProduct_RecyclerView = (RecyclerView) findViewById(R.id.Order_products_recyclerView);
        OrderProduct_RecyclerView.setLayoutManager(layoutManager);
        OrderProduct_RecyclerView.setHasFixedSize(true);
        OrderProduct_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        //set data and list adapter
        // her we will use new adapter and we will use begone adapter inside it



        mAdapter = new OrderProductsAdapter(getApplicationContext(), orders.getOrderProductses());
        OrderProduct_RecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_orders_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
