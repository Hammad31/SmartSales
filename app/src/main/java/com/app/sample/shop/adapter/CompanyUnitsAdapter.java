package com.app.sample.shop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.R;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.fragment.Companies_Fragment;
import com.app.sample.shop.fragment.UnitDetailsFragment;
import com.app.sample.shop.model.CompanyUnit;

import java.util.ArrayList;

/**
 * Created by Hammad on 13/01/16.
 */

public class CompanyUnitsAdapter extends ArrayAdapter<CompanyUnit> {
    Context context;
    ArrayList<CompanyUnit> list;
    Companies_Fragment fragment;

    public CompanyUnitsAdapter(Context context, ArrayList<CompanyUnit> objects, Companies_Fragment fragment) {
        super(context, 0, objects);
        list = objects;
        this.context = context;
        this.fragment = fragment;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_unit_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.companyUnitName = (TextView) convertView.findViewById(R.id.unitName);
            holder.companyUnitName.setText(list.get(position).getCUName());
            holder.button = (ImageButton) convertView.findViewById(R.id.unitPage);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), list.get(position).getCUName(), Toast.LENGTH_SHORT).show();
                    Constant.currentComapnyUnit = new CompanyUnit(list.get(position));
                    Fragment f = new UnitDetailsFragment();
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_content, f);
                    fragmentTransaction.commit();

                    Toast.makeText(getContext(), list.get(position).getCUName(), Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(holder);
            return convertView;
        } else {
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.companyUnitName = (TextView) convertView.findViewById(R.id.unitName);
            holder.companyUnitName.setText(list.get(position).getCUName());
            holder.companyUnitLocation = (TextView) convertView.findViewById(R.id.unitLocation);
            holder.companyUnitLocation.setText(list.get(position).getCULocation());
            holder.button = (ImageButton) convertView.findViewById(R.id.unitPage);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), list.get(position).getCUName(), Toast.LENGTH_SHORT).show();
                    Constant.currentComapnyUnit = new CompanyUnit(list.get(position));
                    Fragment f = new UnitDetailsFragment();
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_content, f);
                    fragmentTransaction.commit();

                    Toast.makeText(getContext(), list.get(position).getCUName(), Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(holder);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView companyUnitName, companyUnitLocation;
        public ImageButton button;
    }

}
