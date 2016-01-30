package com.app.sample.shop.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CompaniesAdapter;
import com.app.sample.shop.model.Catalog;
import com.app.sample.shop.model.Company;
import com.app.sample.shop.model.CompanyUnit;
import com.app.sample.shop.model.Product;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Hammad on 13/01/16.
 */
public class Companies_Fragment extends Fragment {
    private View view;
    private ListView listView;
    private ArrayList<Company> list;
    private CompaniesAdapter companiesAdapter;
    private TextView xx;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_companies, null);
        listView = (ListView) view.findViewById(R.id.list);
        list = new ArrayList<>();
//        xx= (TextView)view.findViewById(R.id.textView);
        companiesAdapter = new CompaniesAdapter(getContext(), list, this);
        listView.setAdapter(
                new SlideExpandableListAdapter(
                        companiesAdapter,
                        R.id.expandable_toggle_button,
                        R.id.expandable
                ));
        listView.animate();
//        listView.setAdapter(companiesAdapter);
        new Connect().execute();
        return view;
    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;

        @Override
        protected Void doInBackground(Void... urls) {
            System.out.print("kkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/getCompaniesWithUnite");
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
                JSONArray parentArray = parentObject.getJSONArray("companies");

                for (int i = 0; i < parentArray.length(); i++) {
                    ArrayList<CompanyUnit> UnitList = new ArrayList<>();
                    JSONObject childObject = parentArray.getJSONObject(i);
                    String comName = childObject.getString("comName");
                    String Active = childObject.getString("Active");
                    int UserID = childObject.getInt("UserID");
                    int ComID = childObject.getInt("ComID");
                    String CAccountNO = childObject.getString("CAccountNO");
                    JSONArray UnitArray = childObject.getJSONArray("units");

                    for (int v = 0; v < UnitArray.length(); v++) {
                        JSONObject JSONUnit = UnitArray.getJSONObject(v);
                        String CUName = JSONUnit.getString("CUName");
                        String CULocation = JSONUnit.getString("CULocation");
                        String Street = JSONUnit.getString("Street");
                        String Civil = JSONUnit.getString("Civil");
                        String Region = JSONUnit.getString("Region");
                        String CUCategory = JSONUnit.getString("CUCategory");
                        int CUID = JSONUnit.getInt("CUID");
                        String CUAccountNO = JSONUnit.getString("CUAccountNO");
                        int ComIDForUnit = JSONUnit.getInt("ComID");

                        UnitList.add(new CompanyUnit(CUName, ComIDForUnit, CUAccountNO, CUID, CUCategory, Region, Civil, Street, CULocation, comName));
                    }
                    list.add(new Company(Active, comName, ComID, CAccountNO, UnitList, UserID));
                }
            } catch (Exception e) {

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
//            xx.setText(list.get(0).getCompanyUnits().get(0).getCUName());
            companiesAdapter.notifyDataSetChanged();
            listView.invalidate();
        }


    }

}
