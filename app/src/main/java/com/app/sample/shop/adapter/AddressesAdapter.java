package com.app.sample.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                                //Remove it from the local data set
                                addressList.remove(position);
                                //Remove it from the server
                                //API Here to delete address by id
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

}
