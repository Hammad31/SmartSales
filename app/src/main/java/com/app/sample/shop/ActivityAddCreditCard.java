package com.app.sample.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.sample.shop.model.CreditCard;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.regex.Pattern;

public class ActivityAddCreditCard extends AppCompatActivity {
    private ActionBar actionBar;
    private CreditCardView creditCardView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        initToolbar();
        creditCardView = (CreditCardView) findViewById(R.id.card);
        creditCardView.setType(CardType.AUTO);
        button = (Button) findViewById(R.id.save_card);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void SaveCrditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setName(creditCardView.getCardName());
        creditCard.setNumber(creditCardView.getCardNumber());
        creditCard.setExpireData(creditCardView.getExpiryDate());
        int type = creditCardView.getType();
        String typeString = "";
        switch (type) {
            case 1:
                typeString = "visa";
                break;
            case 2:
                typeString = "mastercard";
                break;
            case 3:
                typeString = "american_express";
                break;
            case 4:
                typeString = "discover";
                break;
            case 5:
                typeString = "visa";
                break;

        }
        type = -1;
        if(Pattern.compile("^5[1-5][0-9]{14}$").matcher(creditCard.getNumber()).matches()) {
            type = 1;
        } else if(Pattern.compile("^3[47][0-9]{13}$").matcher(creditCard.getNumber()).matches()) {
            type = 2;
        } else if(Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$").matcher(creditCard.getNumber()).matches()) {
            type = 3;
        }

        Toast.makeText(getApplicationContext(), "Type is: " + type, Toast.LENGTH_SHORT).show();

        creditCard.setType(typeString);
        Intent intent = new Intent();
        intent.putExtra("CreditCard", creditCard);
        setResult(RESULT_OK, intent);
        finish();

    }

}
