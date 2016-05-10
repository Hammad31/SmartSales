package com.app.sample.shop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.sample.shop.adapter.ProductGridAdapter;
import com.app.sample.shop.model.Cart_Product;
import com.app.sample.shop.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ActivityLastProducts extends AppCompatActivity {

    ArrayList<Product> products;
    RecyclerView recyclerView;
    ProductGridAdapter productGridAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_products);
        products = new ArrayList<>();
        Connect connect = new Connect();
        try {
            connect.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        productGridAdapter = new ProductGridAdapter(getApplicationContext(), products);
        recyclerView.setAdapter(productGridAdapter);

    }

    public class Connect extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/test/GetLastProducts");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject jsonParent = new JSONObject(finalJSON);
                JSONArray array = jsonParent.getJSONArray("Products");
                System.out.println(array.length() + "  IS the size of the array");
                for (int x = 0; x < array.length(); x++) {
                    JSONObject parentObject = array.getJSONObject(x);
                    String name = parentObject.getString("name");
                    int quantity = parentObject.getInt("quantity");
                    int PID = parentObject.getInt("PID");
                    int CUID = parentObject.getInt("CUID");
                    int ComID = parentObject.getInt("ComID");
                    int CATALOGCatalogID = parentObject.getInt("CATALOGCatalogID");
                    String date = parentObject.getString("date");
                    String type = parentObject.getString("type");
                    double price = parentObject.getDouble("price");
                    int like = parentObject.getInt("like");
                    int sales = 0;
                    String info = parentObject.getString("info");
                    String properties = parentObject.getString("properties");
                    JSONArray photos = parentObject.getJSONArray("images");
                    ArrayList<String> images_links = new ArrayList<>();
                    for (int j = 0; j < photos.length(); j++) {
                        images_links.add(photos.getString(j));
                    }
                    Product product = new Product(images_links, price, date, CUID, CATALOGCatalogID, ComID, PID, quantity, info, type, name, like, 0, properties);
                    products.add(product);
                    System.out.println(x + " Added");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage() + " Best Product Exception");
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
    }

}
