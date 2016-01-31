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

import com.app.sample.shop.adapter.AddressesAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.Address;
import com.balysv.materialripple.MaterialRippleLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityAddresses extends AppCompatActivity {
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private AddressesAdapter addressesAdapter;
    private List<Address> addressList;
    private Button buttonAdd;
    private final static int ASK_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);
        addressList = new ArrayList<>();
        initToolbar();
        initAddresses();

        buttonAdd = (Button) findViewById(R.id.buttons);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the add address activity to get new location
                //The new address will be received in
                Intent i = new Intent(getApplicationContext(), ActivityAddAddress.class);
                startActivityForResult(i, ASK_LOCATION);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.addresses_list);
        addressesAdapter = new AddressesAdapter(getApplicationContext(), addressList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addressesAdapter);
        addressesAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    private void initAddresses() {
        //Call API Here to make the addresses ready
        Address a1 = new Address();
        a1.setBuilding("12");
        a1.setRegion("Exit 13");
        a1.setCountry("Saudi Arabia");
        a1.setStreet("King Abdullah");
        a1.setCity("Riyadh");

        Address a2 = new Address();
        a2.setBuilding("12");
        a2.setRegion("Exit 13");
        a2.setCountry("Saudi Arabia");
        a2.setStreet("King Abdullah");
        a2.setCity("Riyadh");
        addressList.add(a1);
        addressList.add(a2);

    }

    private void initToolbar() {
        //Create tool bar in the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Addresses");
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
        addressesAdapter.notifyDataSetChanged();
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
            Address address = (Address) data.getSerializableExtra("Address");
            addressList.add(address);
            //Update the adapter data set
            addressesAdapter.swapItems(addressList);
            //Update view to make the new address appear if no address there
            addressesAdapter.updateView();
            addressesAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
            Toast.makeText(getApplicationContext(), "New Address Saved", Toast.LENGTH_SHORT).show();
        } else {
            //Back button pressed
            Toast.makeText(getApplicationContext(), "Back Presses", Toast.LENGTH_SHORT).show();
        }
    }
}
