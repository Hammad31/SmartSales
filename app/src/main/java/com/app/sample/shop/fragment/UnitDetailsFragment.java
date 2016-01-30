package com.app.sample.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.R;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.CompanyUnit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by LENOVO on 1/18/2016.
 */
public class UnitDetailsFragment extends Fragment {

    /**
     * Created by Hammad on 12/01/16.
     */
    private TextView unitName, unitCity, companyName;
    private View view;
    MapView mMapView;
    private GoogleMap googleMap;
    private CompanyUnit companyUnit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unit_details, null);
        companyUnit = Constant.currentComapnyUnit;
        Constant.currentComapnyUnit = null;

        unitName = (TextView) view.findViewById(R.id.unitName);
        unitCity = (TextView) view.findViewById(R.id.unitCity);
        companyName = (TextView) view.findViewById(R.id.companyName);

        unitName.setText(companyUnit.getCUName().toUpperCase());
        unitCity.setText(companyUnit.getCULocation());
        companyName.setText(companyUnit.getCompanyName());

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        // latitude and longitude
//        double latitude = companyUnit.getLatitude();
//        double longitude = companyUnit.getLongitude();
        double latitude = 24.700300;
        double longitude = 46.816303;
        if (longitude == 0 || latitude == 0){
            Toast.makeText(getContext(), "This unit has not registered place", Toast.LENGTH_LONG).show();
        }
        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(companyUnit.getCUName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // Changing marker icon

        // adding marker
        try {
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    void setUnit(CompanyUnit unit) {
        companyUnit = unit;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
