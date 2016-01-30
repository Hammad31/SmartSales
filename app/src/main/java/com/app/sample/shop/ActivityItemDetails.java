package com.app.sample.shop;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.SQLException;

public class ActivityItemDetails extends AppCompatActivity {
    public static final String EXTRA_OBJCT = "com.app.sample.shop.PRODUCT";

//    private Product product;
    private Product product;
    private ActionBar actionBar;
    private GlobalVariable global;
    private View parent_view;
    private boolean in_cart=false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        parent_view = findViewById(android.R.id.content);
        global = (GlobalVariable) getApplication();
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
        // get extra object
        product = Constant.currentProduct;
        initToolbar();
        ((TextView) findViewById(R.id.title)).setText(product.getName());
        ImageView imageView = (ImageView) findViewById(R.id.image);
        //ImageLoader.getInstance().displayImage("http://192.168.1.119:8080/Project/Image/" + product.getPhoto(), imageView);
        if (product.getPhoto().startsWith("http"))
            ImageLoader.getInstance().displayImage(product.getPhoto(), imageView);
        else
            ImageLoader.getInstance().displayImage("http://hamoha.com/Project/Image/" + product.getPhoto(), imageView);

        ((TextView)findViewById(R.id.price)).setText("$" + product.getPrice());
        ((TextView)findViewById(R.id.likes)).setText(""+product.getLike()+ " Like");
        ((TextView)findViewById(R.id.sales)).setText(""+product.getSales()+ " Sales");
        ((TextView)findViewById(R.id.info)).setText(""+product.getInfo());
        ((TextView)findViewById(R.id.Properties)).setText(""+product.getProperties());
        final Button bt_cart = (Button) findViewById(R.id.bt_cart);

        if(global.isCartExist(product)){
            cartRemoveMode(bt_cart);
        }

        bt_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!in_cart){
                    global.addCart(product);
                    cartRemoveMode(bt_cart);
                    Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_SHORT).show();
                }else{
                    global.removeCart(product);
                    crtAddMode(bt_cart);
                    Snackbar.make(view, "Removed from Cart", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // for system bar in lollipop
        if (Constant.getAPIVerison() >= 5.0) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void cartRemoveMode(Button bt){
        bt.setText("REMOVE FROM CART");
        bt.setBackgroundColor(getResources().getColor(R.color.colorRed));
        in_cart = true;
    }
    private void crtAddMode(Button bt){
        bt.setText("ADD TO CART");
        bt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        in_cart = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_cart:
                dialogCartDetails();
                break;
            case R.id.action_settings:
                Snackbar.make(parent_view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_credit:
                Snackbar.make(parent_view, "Credit Clicked", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.action_about: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(getString(R.string.about_text));
                builder.setNeutralButton("OK", null);
                builder.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }


    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(product.getName());
    }

    private void dialogCartDetails() {

        final Dialog dialog = new Dialog(ActivityItemDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cart_detail);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LinearLayout lyt_notfound = (LinearLayout) dialog.findViewById(R.id.lyt_notfound);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //set data and list adapter
        CartListAdapter mAdapter = new CartListAdapter(this, global.getCart());
        recyclerView.setAdapter(mAdapter);
        ((TextView)dialog.findViewById(R.id.item_total)).setText(" - " + global.getCartItemTotal() + " Items");
        ((TextView)dialog.findViewById(R.id.price_total)).setText(" $ " + global.getCartPriceTotal());
        ((ImageView)dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if(mAdapter.getItemCount()==0){
            lyt_notfound.setVisibility(View.VISIBLE);
        }else{
            lyt_notfound.setVisibility(View.GONE);
        }
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void actionClick(View view){
        switch (view.getId()){
            case R.id.lyt_likes:
                Snackbar.make(view, "Likes Clicked", Snackbar.LENGTH_SHORT).show();
            break;
            case R.id.lyt_sales:
                Snackbar.make(view, "Sales Clicked", Snackbar.LENGTH_SHORT).show();
            break;

        }
    }

}
