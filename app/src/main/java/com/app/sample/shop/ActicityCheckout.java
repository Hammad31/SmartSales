package com.app.sample.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Spinner;

import com.app.sample.shop.adapter.CartListAdapter;

public class ActicityCheckout extends AppCompatActivity {
    private CartListAdapter cartListAdapter;
    private RecyclerView recyclerView;
    private Spinner addressSpinner, creditcardSpinner, shipperSpinner;
    private Button add_address_button, add_creditcard_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_checkout);

    }
}
