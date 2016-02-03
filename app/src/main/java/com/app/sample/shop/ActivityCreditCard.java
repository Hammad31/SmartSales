package com.app.sample.shop;

import android.content.Intent;
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
import com.app.sample.shop.model.CreditCard;

import java.util.ArrayList;
import java.util.List;

public class ActivityCreditCard extends AppCompatActivity {
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private CreditCardsAdapter creditCardsAdapter;
    private List<CreditCard> creditCardList;
    private Button buttonAdd;
    private final static int ASK_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        creditCardList = new ArrayList<>();
        initToolbar();
        initCreditCards();

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
        CreditCard a1 = new CreditCard();
        CreditCard a2 = new CreditCard();
        a1.setCardID(1);
        a1.setCVVNumber("123");
        a1.setExpireData("04/06");
        a1.setName("Hammad Al Hammad");
        a1.setNumber("5500005555555559");
        a1.setType("Visa");

        a2.setCardID(2);
        a2.setCVVNumber("456");
        a2.setExpireData("10/10");
        a2.setName("Mohammed Al Yahaya");
        a2.setNumber("5500005555555559");
        a2.setType("Visa");

        creditCardList.add(a1);
        creditCardList.add(a2);

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
}
