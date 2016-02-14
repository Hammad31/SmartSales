package com.app.sample.shop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.Address;
import com.app.sample.shop.model.CreditCard;
import com.app.sample.shop.model.Shipper;

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

public class ActicityCheckout extends AppCompatActivity {
    private CartListAdapter cartListAdapter;
    private RecyclerView recyclerView;
    private Spinner addressSpinner, creditcardSpinner, shipperSpinner;
    private Button add_address_button, add_creditcard_button;
    private List<Shipper> shipperList;
    private List<CreditCard> creditCardList;
    private List<Address> addressList;
    private List<String> addressStrings, CreditCardStrings, ShippersStrings;
    private SessionManager sessionManager;
    ArrayAdapter<String> creditcardAdapter, addressAdapter, shipperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_checkout);
        shipperList = new ArrayList<>();
        creditCardList = new ArrayList<>();
        addressList = new ArrayList<>();
        addressStrings = new ArrayList<>();
        CreditCardStrings = new ArrayList<>();
        ShippersStrings = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());


        initAdapters();

        new AddressesConnect().execute();
        new ShippersConnect().execute();
        new CreditCardConnect().execute();

    }

    private void initAdapters() {
        addressSpinner = (Spinner) findViewById(R.id.spinner_address);
        addressAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, addressStrings);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();
        addressSpinner.invalidate();

        creditcardSpinner = (Spinner) findViewById(R.id.spinner_creditcard);
        creditcardAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, CreditCardStrings);
        creditcardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditcardSpinner.setAdapter(creditcardAdapter);
        creditcardAdapter.notifyDataSetChanged();
        creditcardSpinner.invalidate();


        shipperSpinner = (Spinner) findViewById(R.id.spinner_shipper);
        shipperAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ShippersStrings);
        shipperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipperSpinner.setAdapter(shipperAdapter);

    }

    private class ShippersConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/test/getShippers");
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
                JSONArray parentArray = parentObject.getJSONArray("Shippers");

                if (parentArray.length() < 1) {
                    empty = true;
                } else {
                    empty = false;
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject childObject = parentArray.getJSONObject(i);
                        String SID = childObject.getString("SID");
                        String UName = childObject.getString("UName");
                        String UserID = childObject.getString("UserID");
                        String SAccount = childObject.getString("SAccount");
                        double ShippingPrice = childObject.getDouble("ShippingPrice");
                        int ShippingTime = childObject.getInt("ShippingTime");
                        Shipper shipper = new Shipper();
                        shipper.setSAccount(SAccount);
                        shipper.setShippingPrice(ShippingPrice);
                        shipper.setShippingTime(ShippingTime);
                        shipper.setUserID(UserID);
                        shipper.setSID(SID);

                        if (ShippingTime >= 2)
                            ShippersStrings.add(UName + ", " + "$" + ShippingPrice + ", " + ShippingTime + " Days");
                        else
                            ShippersStrings.add(UName + ", " + "$" + ShippingPrice + ", One Day");

                        shipperList.add(shipper);
                    }
                }
            } catch (Exception e) {
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
            if (!empty) {
                Toast.makeText(getApplicationContext(), "Shippers :" + shipperAdapter.getCount(), Toast.LENGTH_SHORT).show();
                shipperAdapter.notifyDataSetChanged();
                shipperSpinner.invalidate();

            } else {
            }
        }
    }

    private class AddressesConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/getAddresses?CID=" + sessionManager.getCurrentCustomerID());
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
                JSONArray parentArray = parentObject.getJSONArray("Addresses");

                if (parentArray.length() < 1) {
                    empty = true;
                } else {
                    empty = false;
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject childObject = parentArray.getJSONObject(i);
                        String CID = childObject.getString("CID");
                        String country = childObject.getString("country");
                        String city = childObject.getString("city");
                        String region = childObject.getString("region");
                        String street = childObject.getString("street");
                        String building = childObject.getString("building");
                        double lon = childObject.getDouble("longitude");
                        double lat = childObject.getDouble("latitude");
                        int AID = childObject.getInt("AID");
                        Address address = new Address();

                        address.setRegion(region);
                        address.setStreet(street);
                        address.setBuilding(building);
                        address.setLongitude(lon);
                        address.setLatitude(lat);
                        address.setCountry(country);
                        address.setCity(city);
                        address.setAID(AID);
                        addressStrings.add(region + ", " + street + ", " + building);
                        //addressStrings.add(country + ", " + city + ", " + region + ", " + street + ", " + building);
                        addressList.add(address);
                    }
                }
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!empty) {
                Toast.makeText(getApplicationContext(), "Addresses :" + addressAdapter.getCount(), Toast.LENGTH_SHORT).show();
                addressAdapter.notifyDataSetChanged();
                addressSpinner.invalidate();
            } else {
            }
        }
    }

    private class CreditCardConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/getCreditCards?CID=" + sessionManager.getCurrentCustomerID());
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
                JSONArray parentArray = parentObject.getJSONArray("CreditCards");

                if (parentArray.length() < 1) {
                    empty = true;
                } else {
                    empty = false;
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject childObject = parentArray.getJSONObject(i);
                        String CID = childObject.getString("CID");
                        String number = childObject.getString("number");
                        String name = childObject.getString("name");
                        String expireDate = childObject.getString("expireDate");
                        int type = childObject.getInt("type");
                        String CVVNumber = childObject.getString("CVVNumber");
                        int CardID = childObject.getInt("CardID");

                        CreditCard creditCard = new CreditCard();
                        creditCard.setType(type);
                        creditCard.setCVVNumber(CVVNumber);
                        creditCard.setCardID(CardID);
                        creditCard.setExpireData(expireDate);
                        creditCard.setName(name);
                        creditCard.setNumber(number);

                        String num = String.valueOf(number);
                        num = num.replaceFirst(".{10}", "**********");
                        CreditCardStrings.add(num);
                        creditCardList.add(creditCard);
                    }
                }
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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!empty) {
                Toast.makeText(getApplicationContext(), "CreditCard: " + creditcardAdapter.getCount(), Toast.LENGTH_SHORT).show();
                creditcardAdapter.notifyDataSetChanged();
                creditcardSpinner.invalidate();
            } else {
            }
        }
    }


}
