package com.app.sample.shop;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityAddAddress extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;
    TextView tvCountry, tvCity, tvRegion, tvStreet;
    EditText txtBuldingNum;
    Button btnSaveAddress;
    MapView mMapView;
    private GoogleMap googleMap;
    double latitude;
    double longitude;
    private boolean registered = false;
    private ProgressDialog dialog;
    private ActionBar actionBar;
    private com.app.sample.shop.model.Address newAddress;

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
        btnSaveAddress = (Button) findViewById(R.id.save_address);

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

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Addresses");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        googleMap = mMapView.getMap();
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


    }

    @Override
    public void onLocationChanged(Location location) {
        if (!registered) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(getApplicationContext(), "Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show();
            registered = true;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
            initMap();

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
                String full_address, region, street;
                full_address = address.get(0).getAddressLine(0).substring(5);
                street = full_address.split(",")[0].substring(4);
                region = full_address.split(",")[1];

                tvCountry.setText(address.get(0).getCountryName());
                tvCity.setText(address.get(0).getLocality());
                tvRegion.setText(region);
                tvStreet.setText(street);
                dialog.hide();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }

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

    private void SaveAddress(){
        //API -- HERE
        newAddress.setCountry(tvCountry.getText().toString());
        newAddress.setCity(tvCity.getText().toString());
        Toast.makeText(getApplicationContext(), newAddress.getCity(), Toast.LENGTH_SHORT).show();
        onBackPressed();

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

}
