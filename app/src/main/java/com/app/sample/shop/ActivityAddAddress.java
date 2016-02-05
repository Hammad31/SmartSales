package com.app.sample.shop;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.JSON.JSONParser;
import com.app.sample.shop.data.SessionManager;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityAddAddress extends AppCompatActivity implements LocationListener, GoogleMap.OnMapLongClickListener {
    LocationManager locationManager;
    String provider;
    TextView tvCountry, tvCity, tvRegion, tvStreet;
    EditText txtBuldingNum;
    ActionProcessButton btnSaveAddress;
    MapView mMapView;
    private GoogleMap googleMap;
    double latitude;
    double longitude;
    private boolean registered = false;
    private ProgressDialog dialog;
    private ActionBar actionBar;
    private com.app.sample.shop.model.Address newAddress;
    private String url_add_address = "http://hamoha.com/Project/addNewAddress";
    private SessionManager sessionManager;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    boolean checkTransfer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initToolbar();

        tvCountry = (TextView) findViewById(R.id.country);
        tvCity = (TextView) findViewById(R.id.city);
        tvRegion = (TextView) findViewById(R.id.region);
        tvStreet = (TextView) findViewById(R.id.street);
        txtBuldingNum = (EditText) findViewById(R.id.building);
        btnSaveAddress = (ActionProcessButton) findViewById(R.id.save_address);
        btnSaveAddress.setMode(ActionProcessButton.Mode.ENDLESS);

        newAddress = new com.app.sample.shop.model.Address();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check which is better (WIFI, GPS ...)
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Toast.makeText(getApplicationContext(), "Location is good", Toast.LENGTH_SHORT).show();
        }
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        sessionManager = new SessionManager(getApplicationContext());

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSaveAddress.setProgress(1);
                SaveAddress();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Your Location");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

    }

    private void initToolbar() {
        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Add Address");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //When the user click back button,
                //No address will saved
                //The activity result is RESULT_CANCELED

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        googleMap = mMapView.getMap();
        googleMap.setOnMapLongClickListener(this);

    }

    private void UpdateMapMarker() {
        googleMap.clear();

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        try {
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            dialog.hide();
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            String full_address, region, street;
            full_address = address.get(0).getAddressLine(0).substring(5);
            street = full_address.split(",")[0].substring(4);
            region = full_address.split(",")[1];

            tvCountry.setText(address.get(0).getCountryName());
            tvCity.setText(address.get(0).getLocality());
            tvRegion.setText(region);
            tvStreet.setText(street);
//            dialog.hide();

        } catch (IOException e) {
        } catch (Exception e) {
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        if (!registered) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            registered = true;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
            initMap();
            UpdateMapMarker();


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!registered) {
            locationManager.requestLocationUpdates(provider, 10, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    public boolean CheckInput() {
        if (tvCountry.getText().toString().length() > 0)
            if (tvCity.getText().toString().length() > 0)
                if (txtBuldingNum.getText().toString().length() > 0)
                    if (tvRegion.getText().toString().length() > 0)
                        if (latitude != 0)
                            if (longitude != 0)
                                if (tvStreet.getText().toString().length() > 0)
                                    if (sessionManager.getCurrentCustomerID().length() > 0)
                                        return true;
        return false;

    }

    private void SaveAddress() {
        if (!CheckInput()) {
            btnSaveAddress.setProgress(-1);
            return;
        }
        newAddress.setCountry(tvCountry.getText().toString());
        newAddress.setCity(tvCity.getText().toString());
        newAddress.setBuilding(txtBuldingNum.getText().toString());
        newAddress.setRegion(tvRegion.getText().toString());
        newAddress.setLatitude(latitude);
        newAddress.setLongitude(longitude);
        newAddress.setStreet(tvStreet.getText().toString());
        newAddress.setCID(sessionManager.getCurrentCustomerID());
        //API -- HERE
        //Save The address to the server
        new SaveAddress().execute();
    }

    public void Complete() {
        Intent intent = new Intent();
        intent.putExtra("Address", newAddress);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        longitude = latLng.longitude;
        latitude = latLng.latitude;
        UpdateMapMarker();
    }

    private class SaveAddress extends AsyncTask<Void, Void, Void> {
        String AID;

        @Override
        protected Void doInBackground(Void... args) {
            checkTransfer = false;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CID", newAddress.getCID()));
            params.add(new BasicNameValuePair("country", newAddress.getCountry()));
            params.add(new BasicNameValuePair("city", newAddress.getCity()));
            params.add(new BasicNameValuePair("region", newAddress.getRegion()));
            params.add(new BasicNameValuePair("street", newAddress.getStreet()));
            params.add(new BasicNameValuePair("building", newAddress.getBuilding()));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            json = jParser.makeHttpRequest(url_add_address, "GET", params);
            String s = null;

            try {
                s = json.getString("info");
                Log.d("Msg", json.getString("info"));
                if (s.equals("success")) {
                    checkTransfer = true;
                    AID = json.getString("AddressID");
                    Log.d("Msg", AID);
                } else {
                    Log.d("Msg", json.getString("Message"));
                    checkTransfer = false;
                }
            } catch (Exception e) {
                Log.d("Msg", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (checkTransfer) {
                btnSaveAddress.setProgress(100);
                Complete();
            } else {
                btnSaveAddress.setProgress(0);
            }
        }

    }

}
