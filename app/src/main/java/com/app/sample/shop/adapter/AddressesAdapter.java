package com.app.sample.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.ActivityAddresses;
import com.app.sample.shop.R;
import com.app.sample.shop.model.Address;
import com.balysv.materialripple.MaterialRippleLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 1/29/2016.
 */
public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {
    private List<Address> addressList = new ArrayList();
    private Context context;
    private ActivityAddresses activityAddresses;
    public LinearLayout addressesLayout, noAddresses;


    public AddressesAdapter(Context context1, List list, ActivityAddresses activityAddresses) {
        this.context = context1;
        this.addressList = list;
        this.activityAddresses = activityAddresses;
        addressesLayout = (LinearLayout) activityAddresses.findViewById(R.id.addresses_layout);
        noAddresses = (LinearLayout) activityAddresses.findViewById(R.id.no_addresses);
        //If no cards, it will be empty addresses massage
        updateView();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.address_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void swapItems(List<Address> items) {
        //Update the data set
        this.addressList = items;
        notifyDataSetChanged();
    }


    public void updateView() {
        //If empty will show new linear layout with image and text view
        //To let the user know that he has no addresses
        if (addressList.size() <= 0) {
            addressesLayout.setVisibility(View.GONE);
            noAddresses.setVisibility(View.VISIBLE);
        } else {
            //If the use has address, new or old one..
            //The addressesLayout will come and noAddresses will go
            addressesLayout.setVisibility(View.VISIBLE);
            noAddresses.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.counter.setText("" + (position + 1));
        holder.country_city.setText(addressList.get(position).getCountry() + " - " + addressList.get(position).getCity());
        holder.region.setText(addressList.get(position).getRegion());
        holder.street.setText(addressList.get(position).getStreet());
        final int i = position;
        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //AlertDialog is yes/no dialog
                //If the user click yes, the address will deleted
                //No, Address will not be deleted
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activityAddresses);
                builder1.setMessage("Are you sure that you want to delete your address?");
                builder1.setCancelable(true);
                builder1.setTitle("Delete Address");

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Remove it from the server
                                //Remove it from the local data set
                                //API Here to delete address by id
                                Connect connect = new Connect();
                                connect.AID = addressList.get(position).getAID();
                                connect.execute();
                                addressList.remove(position);
                                notifyDataSetChanged();
                                updateView();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(context, "Address did not deleted", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView counter;
        public TextView country_city;
        public TextView region;
        public TextView street;
        public MaterialRippleLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            counter = (TextView) v.findViewById(R.id.counter);
            country_city = (TextView) v.findViewById(R.id.country_city);
            region = (TextView) v.findViewById(R.id.region);
            street = (TextView) v.findViewById(R.id.street);
            lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.address_card);
        }

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        StringBuffer buffer;
        int AID = 0;
        boolean deleted = false;
        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://hamoha.com/Project/deleteAddress?AID=" + AID);
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
                System.out.println(parentObject.getString("info"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
        }
    }

}
