package com.app.sample.shop;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.Cart_Product;
import com.app.sample.shop.model.CreditCard;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class ActivityItemDetails extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {
    public static final String EXTRA_OBJCT = "com.app.sample.shop.PRODUCT";
    private SliderLayout mDemoSlider;
    SessionManager sessionManager;
    //    private Product product;
    private Product product;
    private ActionBar actionBar;
    private GlobalVariable global;
    private View parent_view;
    private boolean in_cart = false;
    private boolean liked_before;
    private ImageView icon_like;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        parent_view = findViewById(android.R.id.content);
        global = (GlobalVariable) getApplication();
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        product = Constant.currentProduct;
        sessionManager = new SessionManager(getApplicationContext());
        icon_like = (ImageView) findViewById(R.id.icon_like);

        try {
            new CheckProductLike().execute().get();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Check: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        // get extra object
        initToolbar();
        ((TextView) findViewById(R.id.title)).setText(product.getName());
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> url_maps = new HashMap<String, String>();
        for (int i = 0; i < product.getPhoto().size(); i++) {
            if (product.getPhoto().get(i).startsWith("http"))
                url_maps.put("" + i, product.getPhoto().get(i));
            else
                url_maps.put("" + i, "http://hamoha.com/Project/" + product.getPhoto().get(i));
        }
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        ((TextView) findViewById(R.id.price)).setText("$" + product.getPrice());
        ((TextView) findViewById(R.id.likes)).setText("" + product.getLike() + " Like");
        ((TextView) findViewById(R.id.sales)).setText("" + product.getSales() + " Sales");
        ((TextView) findViewById(R.id.info)).setText("" + product.getInfo());
        ((TextView) findViewById(R.id.Properties)).setText("" + product.getProperties());
        final Button bt_cart = (Button) findViewById(R.id.bt_cart);

        if (global.isCartExist(product)) {
            cartRemoveMode(bt_cart);
        }


        bt_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!in_cart) {
                    Cart_Product cart_product = new Cart_Product();
                    cart_product.ProductID = product.getPID();
                    cart_product.Quantity = 1;
                    global.SaveToDatabase(product, 1);
                    global.addCart(product, cart_product);
                    cartRemoveMode(bt_cart);
                    Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_SHORT).show();
                } else {
                    global.DeleteFromDatabase(product);
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

    private void cartRemoveMode(Button bt) {
        bt.setText("REMOVE FROM CART");
        bt.setBackgroundColor(getResources().getColor(R.color.colorRed));
        in_cart = true;
    }

    private void crtAddMode(Button bt) {
        bt.setText("ADD TO CART");
        bt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        in_cart = false;
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
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
        ((TextView) dialog.findViewById(R.id.item_total)).setText(" - " + global.getCartItemTotal() + " Items");
        ((TextView) dialog.findViewById(R.id.price_total)).setText(" $ " + global.getCartPriceTotal());
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (mAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void actionClick(View view) {
        switch (view.getId()) {
            case R.id.lyt_likes:
                if (!sessionManager.isLoggedIn()) {
                    Snackbar.make(view, "You must sign in :)", Snackbar.LENGTH_SHORT).show();

                } else {
                    if (liked_before) {
                        try {
                            new UnLikeProduct().execute().get();
                            Snackbar.make(view, "UnLiked", Snackbar.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "UnLikeProduct: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        try {
                            new LikeProduct().execute().get();
                            Snackbar.make(view, "Liked", Snackbar.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "LikeProduct: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.lyt_sales:
                Snackbar.make(view, "Sales Clicked", Snackbar.LENGTH_SHORT).show();
                break;

        }
    }

    private void MakeItLiked() {
        icon_like.setImageResource(R.drawable.ic_love_medium);
        liked_before = true;
        ((TextView) findViewById(R.id.likes)).setText("" + product.getLike() + " Like");
    }

    private void MakeItUnLiked() {
        icon_like.setImageResource(R.drawable.ic_no_love_medium);
        liked_before = false;
        ((TextView) findViewById(R.id.likes)).setText("" + product.getLike() + " Like");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    //


    private class CheckProductLike extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        String out = "";

        @Override
        protected Void doInBackground(Void... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/CheckProductLike?CID=" + sessionManager.getCurrentCustomerID() + "&PID=" + product.getPID());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                liked_before = !parentObject.getBoolean("Rated");
                if (liked_before) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MakeItLiked();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MakeItUnLiked();
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println("Exception - : Check : " + e.getMessage());
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
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
        }
    }

    private class UnLikeProduct extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;

        @Override
        protected Void doInBackground(Void... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/UnLikeProduct?CID=" + sessionManager.getCurrentCustomerID() + "&PID=" + product.getPID());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                String status = parentObject.getString("Status");
                if (!status.equals("Fail")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            product.setLike(product.getLike() - 1);
                            MakeItUnLiked();
                        }
                    });
                } else
                    System.out.println("UnLike Failed");
            } catch (Exception e) {
                System.out.println("Exception - : Unlike : " + e.getMessage());
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
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
        }
    }

    private class LikeProduct extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;

        @Override
        protected Void doInBackground(Void... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/LikeProduct?CID=" + sessionManager.getCurrentCustomerID() + "&PID=" + product.getPID());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                String status = parentObject.getString("Status");
                if (!status.equals("Fail")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            product.setLike(product.getLike() + 1);
                            MakeItLiked();
                        }
                    });
                } else
                    System.out.println("Like Failed");
            } catch (Exception e) {
                System.out.println("Exception - : Like : " + e.getMessage());
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
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
        }
    }


}
