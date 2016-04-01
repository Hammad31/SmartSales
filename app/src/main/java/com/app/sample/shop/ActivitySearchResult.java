package com.app.sample.shop;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.shop.JSON.JSONParser;
import com.app.sample.shop.adapter.CatalogAdapter;
import com.app.sample.shop.adapter.ProductGridAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.model.Search;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearchResult extends AppCompatActivity {

    JSONParser jParser = new JSONParser();
    JSONObject json;
    private RecyclerView Product_RecyclerView;
    private ProductGridAdapter mAdapter;
    private ArrayList<Product> productsList;
    private ActionBar actionBar;
    Search search ;
    TextView xx2;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_search_result);

        productsList = new ArrayList<>();
        xx2= (TextView) findViewById(R.id.textView11);
        img= (ImageView) findViewById(R.id.imageView);
        img.setVisibility(View.GONE);
        xx2.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        Product_RecyclerView = (RecyclerView) findViewById(R.id.product_recyclerView);
        Product_RecyclerView.setLayoutManager(layoutManager);
        Product_RecyclerView.setHasFixedSize(true);
        Product_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        Product_RecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        search = Constant.search;
        //set data and list adapter
        // her we will use new adapter and we will use begone adapter inside it
        initToolbar();
        mAdapter = new ProductGridAdapter(getApplicationContext(), productsList);
        Product_RecyclerView.setAdapter(mAdapter);
        // here check IF the Search normal OR advance
        Bundle SearchRequest = getIntent().getExtras();
        if(SearchRequest.getString("SearchType").equals("Advance")){
            new ConnectSEO().execute();
        }else {
            new ConnectNormalSearch().execute();
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Back button clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Search Result");
    }

    /////////////////////////////
    private class ConnectSEO extends AsyncTask<Void, Void, Void> {
        String dd="";
        @Override
        protected Void doInBackground(Void... args) {
            String url_login = "http://hamoha.com/Project/AdvanceSearch";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SearchText", search.getSearchText().trim()));
            params.add(new BasicNameValuePair("SearchCatalog", search.getSearchCatalog()));
            params.add(new BasicNameValuePair("SearchBrand", search.getSearchBrand()));
            params.add(new BasicNameValuePair("SearchLocation", search.getSearchLocation()));
            params.add(new BasicNameValuePair("SearchPrice", search.getSearchPrice()));
            params.add(new BasicNameValuePair("SearchSort", search.getSearchSort()));
            json = jParser.makeHttpRequest(url_login, "GET", params);

            try {
                if(json.length()>0) {
                    JSONArray ProductsArray = json.getJSONArray("Products");
                    for (int v = 0; v < ProductsArray.length(); v++) {
                        JSONObject JSONPro = ProductsArray.getJSONObject(v);
                        String name = JSONPro.getString("name");
                        int quantity = JSONPro.getInt("quantity");
                        int PID = JSONPro.getInt("PID");
                        int CUID = JSONPro.getInt("CUID");
                        int ComID = JSONPro.getInt("ComID");
                        int CATALOGCatalogID = JSONPro.getInt("CATALOGCatalogID");
                        String date = JSONPro.getString("date");
                        String type = JSONPro.getString("type");
                        double price = JSONPro.getDouble("price");
                        int like = JSONPro.getInt("like");
                        int sales = JSONPro.getInt("sales");
                        String info = JSONPro.getString("info");
                        String properties = JSONPro.getString("properties");

                        JSONArray photos = JSONPro.getJSONArray("images");
                        ArrayList<String> images_links = new ArrayList<>();
                        for (int j = 0; j < photos.length(); j++) {
                            images_links.add(photos.getString(j));
                        }

                        productsList.add(new Product(images_links, price, date, CUID, CATALOGCatalogID, ComID, PID, quantity, info, type, name, like, sales, properties));
                    }
                }
            } catch (JSONException e) {
//                 TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
            Product_RecyclerView.invalidate();
            if (productsList.isEmpty()){
                xx2.setVisibility(View.VISIBLE);
                xx2.setText("There Is No Match Products !!");
                img.setVisibility(View.VISIBLE);
            }
        }
    }
    //////////////////////////////////////////////
    /////////////////////////////
    private class ConnectNormalSearch extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            String url_login = "http://hamoha.com/Project/NormalSearch";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SearchText", search.getSearchText().trim()));
            json = jParser.makeHttpRequest(url_login, "GET", params);

            try {
                if(json.length()>0) {
                    JSONArray ProductsArray = json.getJSONArray("Products");
                    for (int v = 0; v < ProductsArray.length(); v++) {
                        JSONObject JSONPro = ProductsArray.getJSONObject(v);
                        String name = JSONPro.getString("name");
                        int quantity = JSONPro.getInt("quantity");
                        int PID = JSONPro.getInt("PID");
                        int CUID = JSONPro.getInt("CUID");
                        int ComID = JSONPro.getInt("ComID");
                        int CATALOGCatalogID = JSONPro.getInt("CATALOGCatalogID");
                        String date = JSONPro.getString("date");
                        String type = JSONPro.getString("type");
                        double price = JSONPro.getDouble("price");
                        int like = JSONPro.getInt("like");
                        int sales = JSONPro.getInt("sales");
                        String info = JSONPro.getString("info");
                        String properties = JSONPro.getString("properties");
                        JSONArray photos = JSONPro.getJSONArray("images");
                        ArrayList<String> images_links = new ArrayList<>();
                        for (int j = 0; j < photos.length(); j++) {
                            images_links.add(photos.getString(j));
                        }

                        productsList.add(new Product(images_links, price, date, CUID, CATALOGCatalogID, ComID, PID, quantity, info, type, name, like, sales, properties));
                    }
                }
            } catch (JSONException e) {
//                 TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
            Product_RecyclerView.invalidate();
            if (productsList.isEmpty()){
                xx2.setVisibility(View.VISIBLE);
                xx2.setText("There Is No Match Products !!");
                img.setVisibility(View.VISIBLE);
            }
        }
    }
    //////////////////////////////////////////////
}





  /*
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/c50986936280b50705b4c186b2a3cb26,0.jpg", 3, "3", 3, 3, 3, 3, 3, "r", "f", "f", 3, 3, "d"));
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/455df6c9e63ba70c9ba2752e43c00cbd,0.jpg", 3, "3", 3, 3, 3, 1, 3, "r", "f", "f", 3, 3, "d"));
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/229671d7032c085f008f910d8cb87952,0.jpg", 3, "3", 3, 3, 3, 2, 3, "r", "f", "f", 3, 3, "d"));
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/5bbf4e3594615723ae33880c5a7eb955,0.jpg",3,"3",3,3,3,4,3,"r","f","f",3,3,"d"));
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/c50986936280b50705b4c186b2a3cb26,0.jpg",3,"3",3,3,3,5,3,"r","f","f",3,3,"d"));
        productsList.add(new Product("http://sem3-idn.s3-website-us-east-1.amazonaws.com/a9de05cac3ff3dc40965785a953f19bb,0.jpg",3,"3",3,3,3,6,3,"r","f","f",3,3,"d"));
       */
