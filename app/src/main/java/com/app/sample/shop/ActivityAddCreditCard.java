package com.app.sample.shop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.sample.shop.JSON.JSONParser;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.CreditCard;
import com.dd.processbutton.iml.ActionProcessButton;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddCreditCard extends AppCompatActivity {
    private ActionBar actionBar;
    private CreditCardView creditCardView;
    private ActionProcessButton button;
    private CreditCard creditCard;
    private boolean saved = false;
    SessionManager sessionManager;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    private String url_add_card = "http://hamoha.com/Project/addCreditCard";
    private EditText input_cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        initToolbar();
        sessionManager = new SessionManager(getApplicationContext());
        creditCard = new CreditCard();
        creditCardView = (CreditCardView) findViewById(R.id.card);
        creditCardView.setType(CardType.AUTO);
        input_cvv = (EditText) findViewById(R.id.input_cvv);
        button = (ActionProcessButton) findViewById(R.id.save_card);
        button.setMode(ActionProcessButton.Mode.ENDLESS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setProgress(1);
                SaveCrditCard();
            }
        });

    }

    private void initToolbar() {
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Add Credit Card");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //When the user click back button,
                //No card will saved
                //The activity result is RESULT_CANCELED

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean CheckInput() {
        try {
            if (creditCardView.getCardName().length() > 0)
                if (creditCardView.getCardNumber().length() >= 13 && creditCardView.getCardNumber().length() <= 16)
                    if (creditCardView.getExpiryDate().length() > 0)
                        if (input_cvv.getText().toString().length() == 3)
                            if (creditCardView.getType() > 0)
                                return true;
        } catch (Exception e) {

        }
        return false;

    }

    private void SaveCrditCard() {
        if (!CheckInput()) {
            Snackbar.make(getCurrentFocus(), "Please Check Your Input", Snackbar.LENGTH_SHORT).show();
            button.setProgress(0);
            return;
        }

        creditCard = new CreditCard();
        creditCard.setName(creditCardView.getCardName());
        creditCard.setNumber(creditCardView.getCardNumber());
        creditCard.setExpireData(creditCardView.getExpiryDate());
        creditCard.setCVVNumber(input_cvv.getText().toString());
        creditCard.setType(creditCardView.getType());
        new SaveCreditCard().execute();
    }

    private void Complete() {
        Intent intent = new Intent();
        intent.putExtra("CreditCard", creditCard);
        setResult(RESULT_OK, intent);
        finish();
    }


    private class SaveCreditCard extends AsyncTask<Void, Void, Void> {
        String CardID;

        @Override
        protected Void doInBackground(Void... args) {
            saved = false;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CID", sessionManager.getCurrentCustomerID()));
            params.add(new BasicNameValuePair("type", String.valueOf(creditCard.getType())));
            params.add(new BasicNameValuePair("name", creditCard.getName()));
            params.add(new BasicNameValuePair("CVVNumber", creditCard.getCVVNumber()));
            params.add(new BasicNameValuePair("number", creditCard.getNumber()));
            params.add(new BasicNameValuePair("expireDate", creditCard.getExpireData()));
            json = jParser.makeHttpRequest(url_add_card, "GET", params);
            String s = null;

            try {
                s = json.getString("info");
                Log.d("Msg", json.getString("info"));
                if (s.equals("success")) {
                    saved = true;
                    CardID = json.getString("CardID");
                    creditCard.setCardID(Integer.valueOf(CardID));
                    Log.d("Msg", CardID);
                } else {
                    Log.d("Msg", json.getString("Message"));
                    saved = false;
                }
            } catch (Exception e) {
                Log.d("Msg", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (saved) {
                button.setProgress(100);
                Complete();
            } else {
                Snackbar.make(getCurrentFocus(), "Sorry, Card did not saved", Snackbar.LENGTH_SHORT).show();
                button.setProgress(0);
            }
        }

    }


}
