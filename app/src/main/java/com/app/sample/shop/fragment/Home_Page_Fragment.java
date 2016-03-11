package com.app.sample.shop.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.ActivitySearchResult;
import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CatalogAdapter;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.model.Search;
import com.app.sample.shop.widget.DividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hammad on 12/01/16.
 */
public class Home_Page_Fragment extends android.support.v4.app.Fragment {
    private View view;
    private RecyclerView bestProduct_RecyclerView;
    private GlobalVariable global;
    private CatalogAdapter mAdapter;
    private ArrayList<Catalog> catalog;
    private ArrayList<String> CatalogsList;
    private ArrayList<String> BrandsList;
    private ArrayList<String> LocationList;
    Search search = new Search();
    //    TextView xx;
    ImageButton imageButton ;
    private static final String[]SortList = {"Price:Low To High" ,"Price:High To Low" ,"More Rating", "More Sales", "Recent Added"};
    private static final String[]PriceList = {"All Prices","From 1$ To 1000$", "From 1000$ To 1500$", "From 1500$ To 2500$","From 2500$ To 4500$", "Up 4500$"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        catalog = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_home_page, null);
        global = (GlobalVariable) getActivity().getApplication();

        // for search
        BrandsList = new ArrayList<>();
        LocationList = new ArrayList<>();
        CatalogsList= new ArrayList<>();
        new ConnectForSearch().execute();

        imageButton=(ImageButton) view.findViewById(R.id.imageButton);
//         xx = (TextView) view.findViewById(R.id.textViewmm);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        bestProduct_RecyclerView = (RecyclerView) view.findViewById(R.id.best_product_recyclerView);
        bestProduct_RecyclerView.setLayoutManager(layoutManager);
        bestProduct_RecyclerView.setHasFixedSize(true);
        bestProduct_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        bestProduct_RecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // here for Advance Search
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSearchOptions();
            }
        });
        // here Normal Search
        SearchView searchView = (SearchView)  view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                when user Submit search button (The Normal Search)
                callSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
//              no thing do here
                return true;
            }
            public void callSearch(String query) {
                search.setSearchText(query);
                search.setSearchCatalog("undefined");
                search.setSearchBrand("undefined");
                search.setSearchPrice("undefined");
                search.setSearchLocation("undefined");
                search.setSearchSort("undefined");
                Constant.search = search;
                // call new fragment
                Intent i = new Intent(getContext(), ActivitySearchResult.class);
                i.putExtra("SearchType", "Normal");
                startActivity(i);
            }
        });

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        //set data and list adapter
        // her we will use new adapter and we will use begone adapter inside it
        mAdapter = new CatalogAdapter(getActivity(), catalog);
        bestProduct_RecyclerView.setAdapter(mAdapter);
        new Connect().execute();
        return view;
    }



    private void dialogSearchOptions() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_search_option);

//
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView SearchText = (TextView) dialog.findViewById(R.id.search_text);

        final Spinner spinnerCatalog = (Spinner) dialog.findViewById(R.id.catalog_list_spinner);
        ArrayAdapter<String>adapterCatalog = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,CatalogsList);
        adapterCatalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCatalog.setAdapter(adapterCatalog);

        final Spinner spinnerBrand = (Spinner) dialog.findViewById(R.id.brand_list_spinner);
        ArrayAdapter<String>adapterBrand = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,BrandsList);
        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(adapterBrand);

        final Spinner spinnerPrice = (Spinner) dialog.findViewById(R.id.price_list_spinner);
        ArrayAdapter<String>adapterPrice = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,PriceList);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(adapterPrice);

        final Spinner spinnerLocation = (Spinner) dialog.findViewById(R.id.location_list_spinner);
        ArrayAdapter<String>adapterLocation = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,LocationList);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapterLocation);

        final Spinner spinnerSort = (Spinner) dialog.findViewById(R.id.sort_list_spinner);
        ArrayAdapter<String>adapterSort = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,SortList);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);


        ((ImageView)dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button)dialog.findViewById(R.id.bt_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                xx.setText(spinnerCatalog.getSelectedItem().toString());
                search.setSearchText(SearchText.getText().toString());
                if(SearchText.getText().toString().isEmpty()||SearchText.getText().toString().equals(" ")){
                    SearchText.setError("Please Enter A Text");
                }else {
                    search.setSearchCatalog(spinnerCatalog.getSelectedItem().toString());
                    search.setSearchBrand(spinnerBrand.getSelectedItem().toString());
                    search.setSearchPrice(spinnerPrice.getSelectedItem().toString());
                    search.setSearchLocation(spinnerLocation.getSelectedItem().toString());
                    search.setSearchSort(spinnerSort.getSelectedItem().toString());
                    Constant.search = search;
                    // call new fragment
                    Intent i = new Intent(getContext(), ActivitySearchResult.class);
                    i.putExtra("SearchType","Advance");
                    startActivity(i);
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        @Override
        protected Void doInBackground(Void... urls) {
            System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try{
//                URL url = new URL ("http://192.168.1.119:8080/Project/getCatalogWithProduct");
                URL url = new URL ("http://hamoha.com/Project/getCatalogWithProduct");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line="";
                while((line=reader.readLine())!= null){
                    buffer.append(line);
                }



                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = parentObject.getJSONArray("catalogs");

                for(int i=0;i<parentArray.length();i++) {
                    ArrayList<Product> ProductList = new ArrayList<>();
                    JSONObject childObject = parentArray.getJSONObject(i);
                    String CatalogID = childObject.getString("CatalogID");
                    String Cname = childObject.getString("Cname");
                    JSONArray ProductArray = childObject.getJSONArray("products");

                    for(int v=0;v<ProductArray.length();v++) {
                        JSONObject JSONPro = ProductArray.getJSONObject(v);
                        String name = JSONPro.getString("name");
                        int quantity = JSONPro.getInt("quantity");
                        int PID = JSONPro.getInt("PID");
                        int CUID = JSONPro.getInt("CUID");
                        int ComID = JSONPro.getInt("ComID");
                        int CATALOGCatalogID = JSONPro.getInt("CATALOGCatalogID");
                        String date = JSONPro.getString("date");
                        String type = JSONPro.getString("type");
                        double price = JSONPro.getDouble("price");
                        int like = JSONPro.getInt("like");
                        int sales = JSONPro.getInt("sales");
                        String info = JSONPro.getString("info");
                        String photo = JSONPro.getString("link");
                        String properties = JSONPro.getString("properties");

                        ProductList.add(new Product(photo,price, date, CUID, CATALOGCatalogID, ComID,PID ,quantity,info,type,name,like,sales,properties));
                    }
                    catalog.add(new Catalog(CatalogID,Cname,ProductList));
                }
            }catch (Exception e){

            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
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
            mAdapter.notifyDataSetChanged();
            bestProduct_RecyclerView.invalidate();
//            xx.setText(catalog.get(0).getProduct().get(0).getName());
        }
    }

    private class ConnectForSearch extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try{
                URL url = new URL ("http://hamoha.com/Project/getSearchCat_Com_Location");
//                URL url = new URL ("http://hamoha.com/Project/getSearchCat_Com_Location");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();
                String line="";
                while((line=reader.readLine())!= null){
                    buffer.append(line);
                }
                // Convert to JSON
                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray CatalogsArray = parentObject.getJSONArray("catalog");
                JSONArray BrandsArray = parentObject.getJSONArray("companiesName");
                JSONArray LocationArray = parentObject.getJSONArray("location");

                CatalogsList.add("All Catalogs");
                for(int i=0;i<CatalogsArray.length();i++) {
                    JSONObject childObject = CatalogsArray.getJSONObject(i);
                    String CatalogName = childObject.getString("Cname");
                    CatalogsList.add(CatalogName);
                }
                BrandsList.add("Any Brand");
                for(int i=0;i<BrandsArray.length();i++) {
                    JSONObject childObject = BrandsArray.getJSONObject(i);
                    String BrandName = childObject.getString("comName");
                    BrandsList.add(BrandName);
                }
                LocationList.add("Any City");
                for(int i=0;i<LocationArray.length();i++) {
                    JSONObject childObject = LocationArray.getJSONObject(i);
                    String location = childObject.getString("CULocation");
                    LocationList.add(location);
                }
            }catch (Exception e){

            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
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
//            xx.setText(catalog.get(0).getProduct().get(0).getName());
        }
    }
}