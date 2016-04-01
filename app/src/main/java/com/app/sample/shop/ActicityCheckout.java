package com.app.sample.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.JSON.JSONParser;
import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.Address;
import com.app.sample.shop.model.CreditCard;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Payment;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.model.QuantityChecker;
import com.app.sample.shop.model.Shipper;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.dd.processbutton.iml.ActionProcessButton;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ActicityCheckout extends AppCompatActivity {
    private CartListAdapter cartListAdapter;
    private RecyclerView recyclerView;
    private GlobalVariable globalVariable;
    private Spinner addressSpinner, creditcardSpinner, shipperSpinner;
    private TextView txtTotalPrice;
    private ImageView btnAdd_address, btnAdd_creditcard;
    private ActionProcessButton btnSubmit;
    private List<Shipper> shipperList;
    private List<CreditCard> creditCardList;
    private List<Address> addressList;
    private List<String> addressStrings, CreditCardStrings, ShippersStrings;
    ArrayAdapter<String> creditcardAdapter, addressAdapter, shipperAdapter;
    private final static int ASK_ADDRESS = 100;
    private final static int ASK_CREDITCARD = 200;
    private final static int ASK_PAYMENT = 300;
    private String url_submit_order = "http://hamoha.com/Project/SubmitOrder";
    private SessionManager sessionManager;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    boolean checkTransfer = false;
    private Order order;
    private Payment payment;

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
        order = new Order();
        payment = new Payment();
        globalVariable = (GlobalVariable) getApplication();

        //PayPal Start...
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, globalVariable.payPalConfiguration);
        startService(intent);

        cartListAdapter = new CartListAdapter(getApplicationContext(), globalVariable.getCart());
        recyclerView = (RecyclerView) findViewById(R.id.cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(cartListAdapter);
        cartListAdapter.notifyDataSetChanged();
        recyclerView.invalidate();

        btnAdd_address = (ImageView) findViewById(R.id.btn_add_address);
        btnAdd_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityAddAddress.class);
                startActivityForResult(i, ASK_ADDRESS);
            }
        });

        btnAdd_creditcard = (ImageView) findViewById(R.id.btn_credit_card);
        btnAdd_creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityAddCreditCard.class);
                startActivityForResult(i, ASK_CREDITCARD);

            }
        });

        btnSubmit = (ActionProcessButton) findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = CheckInventory();
                if (!check)
                    return;
                submitPayment();

            }
        });
        initAdapters();

        new AddressesConnect().execute();
        new ShippersConnect().execute();
        new CreditCardConnect().execute();

        shipperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtTotalPrice.setText("Total Price: $" + globalVariable.getCartPriceTotal() + ", Shipping Cost: $" + getShipperPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtTotalPrice = (TextView) findViewById(R.id.total_item_price);
        txtTotalPrice.setText("Total Price: $" + globalVariable.getCartPriceTotal());
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

    private double getShipperPrice() {
        int a = shipperSpinner.getSelectedItemPosition();
        return shipperList.get(a).getShippingPrice();
    }

    //Prepare the payment
    private PayPalItem[] getStuffToBuy() {
        PayPalItem[] items = new PayPalItem[globalVariable.getCart().size()];
        for (int i = 0; i < items.length; i++) {
            Product product = globalVariable.getCart().get(i);
            items[i] = new PayPalItem(product.getName(), globalVariable.cart_products.get(i).Quantity, new BigDecimal(product.getPrice() + ""), "USD", product.getPID() + " - " + product.getComID() + " - ");
        }
        return items;
    }

    private void submitPayment() {
        PayPalItem[] items = getStuffToBuy();
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal tax = new BigDecimal("0");
        BigDecimal shipping = new BigDecimal(getShipperPrice() + "");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);

        PayPalPayment payment = new PayPalPayment(amount, "USD", "sample item", PayPalPayment.PAYMENT_INTENT_SALE);
        payment.items(items).paymentDetails(paymentDetails);
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, globalVariable.payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, ASK_PAYMENT);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ASK_ADDRESS) {
            new AddressesConnect().execute();
        } else if (resultCode == RESULT_OK && requestCode == ASK_CREDITCARD) {
            new CreditCardConnect().execute();
        } else if (requestCode == ASK_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("paymentExample", confirm.toJSONObject().toString(4));
                        Toast.makeText(getApplicationContext(), confirm.getPayment().toString(), Toast.LENGTH_LONG).show();
                        //Payment Done Correctly...
                        btnSubmit.setProgress(1);
                        btnSubmit.setEnabled(false);
                        order.setAdderessID(addressList.get(addressSpinner.getSelectedItemPosition()).getAID());
                        int SID = Integer.valueOf(shipperList.get(shipperSpinner.getSelectedItemPosition()).getSID());
                        order.setShipperSID(SID);
                        order.setCustomerID(Integer.valueOf(sessionManager.getCurrentCustomerID()));
                        order.setShippingCost(getShipperPrice());
                        order.setTotalCost(getShipperPrice() + globalVariable.getCartPriceTotal());
                        order.setShippingTime(shipperList.get(shipperSpinner.getSelectedItemPosition()).getShippingTime());
                        payment.setCardID(creditCardList.get(creditcardSpinner.getSelectedItemPosition()).getCardID());
                        SubmitOrder();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private boolean CheckInventory() {
        for (int i = 0; i < globalVariable.cart_products.size(); i++) {
            QuantityChecker quantityChecker = new QuantityChecker(globalVariable.cart_products.get(i).ProductID, globalVariable.cart_products.get(i).Quantity);
            //No available stock...
            if (!quantityChecker.Check()) {
                Toast.makeText(getApplicationContext(), globalVariable.getCart().get(i).getName() + ", Has no enough quantity", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "You can only buy " + quantityChecker.GetStockQuantity(), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }


    private void SubmitOrder() {
        try {
            new SubmitOrder().execute().get();
            globalVariable.clearCart();
            Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class ShippersConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/getShippers");
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
                        else if (ShippingTime == 1)
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
                shipperSpinner.setSelection(0);
                txtTotalPrice.setText("Total Price: $" + globalVariable.getCartPriceTotal() + ", Shipping Cost: $" + getShipperPrice());
            }
        }
    }

    private class AddressesConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            addressStrings.clear();
            addressList.clear();
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
                addressSpinner.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Addresses :" + addressAdapter.getCount(), Toast.LENGTH_SHORT).show();
                addressAdapter.notifyDataSetChanged();
                addressSpinner.invalidate();
            } else {
                addressSpinner.setVisibility(View.GONE);
            }

        }
    }

    private class CreditCardConnect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        boolean empty = true;

        @Override
        protected Void doInBackground(Void... urls) {
            creditCardList.clear();
            CreditCardStrings.clear();

            //creditCardList = new ArrayList<>();
            //CreditCardStrings = new ArrayList<>();

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
                creditcardSpinner.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "CreditCard: " + creditcardAdapter.getCount(), Toast.LENGTH_SHORT).show();
                creditcardAdapter.notifyDataSetChanged();
                creditcardSpinner.invalidate();
            } else
                creditcardSpinner.setVisibility(View.GONE);
        }
    }


    private class SubmitOrder extends AsyncTask<Void, Void, Void> {
        String OID;

        @Override
        protected Void doInBackground(Void... args) {
            checkTransfer = false;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject parentJSON = new JSONObject();
            try {
                parentJSON.accumulate("shipping_cost", order.getShippingCost());
                parentJSON.put("deliver_days", order.getShippingTime());
                parentJSON.accumulate("AID", order.getAdderessID());
                parentJSON.accumulate("CustomerID", order.getCustomerID());
                parentJSON.accumulate("total_cost", order.getTotalCost());
                parentJSON.accumulate("SID", order.getShipperSID());
                parentJSON.accumulate("CardID", payment.getCardID());
                JSONArray products = new JSONArray();
                for (int i = 0; i < globalVariable.cart_products.size(); i++) {
                    JSONObject product = new JSONObject();
                    product.accumulate("ProductID", globalVariable.cart_products.get(i).ProductID);
                    product.accumulate("Quantity", globalVariable.cart_products.get(i).Quantity);
                    products.put(product);
                }
                parentJSON.accumulate("Order_Products", products);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.add(new BasicNameValuePair("JSON", parentJSON.toString()));
            json = jParser.makeHttpRequest(url_submit_order, "GET", params);
            String s = null;
            try {
                s = json.getString("info");
                Log.d("Msg", json.getString("info"));
                if (s.equals("success")) {
                    checkTransfer = true;
                    OID = json.getString("OrderID");
                    Log.d("Msg", OID);
                    Log.d("Msg", json.getString("Message"));
                } else {
                    Log.d("Msg Not Success", json.getString("Message"));
                    checkTransfer = false;
                }
            } catch (Exception e) {
                Log.d("Msg Exception", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (checkTransfer) {
                btnSubmit.setProgress(100);
                //Complete();
            } else {
                btnSubmit.setProgress(0);
            }
        }

    }




}
