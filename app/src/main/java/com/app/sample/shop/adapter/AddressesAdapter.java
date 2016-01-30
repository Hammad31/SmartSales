package com.app.sample.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public AddressesAdapter(Context context1, List list){
        this.context = context1;
        this.addressList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.address_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.counter.setText("" + (position + 1));
        holder.country_city.setText(addressList.get(position).getCountry() + " - " + addressList.get(position).getCity());
        holder.region.setText("region:" + addressList.get(position).getRegion());
        holder.street.setText("street:" + addressList.get(position).getStreet());
        final int i = position;
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open The Address Page
                Toast.makeText(context, "The Address Clicked is : " + addressList.get(i).getStreet(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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
