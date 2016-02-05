package com.app.sample.shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.sample.shop.adapter.CreditCardsAdapter;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.CreditCard;

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

public class ActivityCreditCard extends AppCompatActivity {
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private CreditCardsAdapter creditCardsAdapter;
    private List<CreditCard> creditCardList;
    private Button buttonAdd;
    private final static int ASK_LOCATION = 100;
    private SessionManager sessionManager;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        sessionManager = new SessionManager(getApplicationContext());
        creditCardList = new ArrayList<>();
        initToolbar();
        initCreditCards();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Your Location");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        buttonAdd = (Button) findViewById(R.id.buttons);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the add address activity to get new location
                //The new address will be received in
                Intent i = new Intent(getApplicationContext(), ActivityAddCreditCard.class);
                startActivityForResult(i, ASK_LOCATION);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.cards_list);
        creditCardsAdapter = new CreditCardsAdapter(getApplicationContext(), creditCardList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(creditCardsAdapter);
        creditCardsAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    private void initCreditCards() {
        //Call API Here to make the cards ready
        new Connect().execute();
    }

    private void initToolbar() {
        //Create tool bar in the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Credit Cards");
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

    @Override
    protected void onResume() {
        super.onResume();
        //While the window is open or the app closed then opened
        creditCardsAdapter.notifyDataSetChanged();
        recyclerView.invalidate();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //This method will called after finsih() in the Add Address Activity
        //It will has the new created address if the result code is RESULT_OK
        //Else, means the user press the back button
        if (resultCode == RESULT_OK) {

            //Get the object from that activity
            CreditCard creditCard = (CreditCard) data.getSerializableExtra("CreditCard");
            creditCardList.add(creditCard);
            //Update the adapter data set
            creditCardsAdapter.swapItems(creditCardList);
            //Update view to make the new address appear if no address there
            creditCardsAdapter.updateView();
            creditCardsAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
            Toast.makeText(getApplicationContext(), "New Credit Card Saved", Toast.LENGTH_SHORT).show();
        } else {
            //Back button pressed
            Toast.makeText(getApplicationContext(), "Back Presses", Toast.LENGTH_SHORT).show();
        }
    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        List<CreditCard> newCreditCards = new ArrayList<>();
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
                        int CardID= childObject.getInt("CardID");

                        CreditCard creditCard = new CreditCard();
                        creditCard.setType(type);
                        creditCard.setCVVNumber(CVVNumber);
                        creditCard.setCardID(CardID);
                        creditCard.setExpireData(expireDate);
                        creditCard.setName(name);
                        creditCard.setNumber(number);
                        newCreditCards.add(creditCard);
                    }
                }
            } catch (Exception e) {
                System.out.println("Hammad" + e.getMessage());
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
            dialog.hide();
            if (!empty) {
                creditCardList.addAll(newCreditCards);
                creditCardsAdapter.swapItems(creditCardList);
                creditCardsAdapter.updateView();
                creditCardsAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            } else {
                System.out.println("It is empty man.!");
            }
        }
    }

}
