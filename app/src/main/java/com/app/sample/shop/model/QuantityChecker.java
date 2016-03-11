package com.app.sample.shop.model;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by LENOVO on 3/10/2016.
 */
public class QuantityChecker {
    private int PID;
    private int Quantity;
    private int StockQuantity;

    public QuantityChecker(int PID, int quantity) {
        this.PID = PID;
        Quantity = quantity;
    }

    public boolean Check() {
        boolean available = false;
        try {
           available = new CheckQuantity().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public int GetStockQuantity(){
        return StockQuantity;
    }
    private class CheckQuantity extends AsyncTask<String, String, Boolean> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/test/getProductDetails?PID=" + PID);
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
                StockQuantity = parentObject.getInt("quantity");
                if (StockQuantity >= Quantity)
                    return true;
                else
                    return false;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
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
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!empty) {
            } else {

            }
        }

    }

}
