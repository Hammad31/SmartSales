package com.app.sample.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.R;
import com.app.sample.shop.fragment.Companies_Fragment;
import com.app.sample.shop.model.Company;

import java.util.ArrayList;

/**
 * Created by Hammad on 13/01/16.
 */
public class CompaniesAdapter extends ArrayAdapter<Company> {
    Context context;
    ArrayList<Company> list;
    Companies_Fragment fragment;
    public CompaniesAdapter(Context context, ArrayList<Company> objects, Companies_Fragment fragment) {
        super(context, 0, objects);
        list = objects;
        this.context = context;
        this.fragment = fragment;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
            holder.companyName.setText(list.get(position).getComName());
            holder.unitsList = (ListView) convertView.findViewById(R.id.unitsList);
            CompanyUnitsAdapter companyUnitsAdapter = new CompanyUnitsAdapter(getContext(), list.get(position).getCompanyUnits(), fragment);
            holder.unitsList.setAdapter(companyUnitsAdapter);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.expandable_toggle_button);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imageButton.setImageResource(R.drawable.ic_decrease);
                    Toast.makeText(getContext(), "Opened", Toast.LENGTH_SHORT).show();
                }
            });

            companyUnitsAdapter.notifyDataSetChanged();
            holder.unitsList.invalidate();
            convertView.setTag(holder);
            return convertView;
        } else {
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
            holder.companyName.setText(list.get(position).getComName());
            holder.unitsList = (ListView) convertView.findViewById(R.id.unitsList);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.expandable_toggle_button);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imageButton.setImageResource(R.drawable.ic_decrease);
                    Toast.makeText(getContext(), "Opened 2", Toast.LENGTH_SHORT).show();
                }
            });
            CompanyUnitsAdapter companyUnitsAdapter = new CompanyUnitsAdapter(getContext(), list.get(position).getCompanyUnits(), fragment);
            holder.unitsList.setAdapter(companyUnitsAdapter);
            companyUnitsAdapter.notifyDataSetChanged();
            holder.unitsList.invalidate();
            convertView.setTag(holder);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView companyName;
        public ListView unitsList;
        public ImageButton imageButton;
    }

}
