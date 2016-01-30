package com.app.sample.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.ActivityMain;
import com.app.sample.shop.R;
import com.app.sample.shop.data.GlobalVariable;
import com.balysv.materialripple.MaterialRippleLayout;

public class Explore_Fragment extends Fragment {

    private View view;
    private MaterialRippleLayout companiesCard, bestProductsCard, lastProductsCard;
    private GlobalVariable global;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, null);
        companiesCard = (MaterialRippleLayout) view.findViewById(R.id.companies);
        bestProductsCard = (MaterialRippleLayout) view.findViewById(R.id.best_products);
        lastProductsCard = (MaterialRippleLayout) view.findViewById(R.id.last_products);
        textView = (TextView) view.findViewById(R.id.test);

        companiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Companies_Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_content, fragment);
                fragmentTransaction.commit();

                Toast.makeText(getContext(), "Companies", Toast.LENGTH_SHORT).show();
            }
        });
        lastProductsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_content, fragment);
                fragmentTransaction.commit();


                Toast.makeText(getContext(), "Last Products", Toast.LENGTH_SHORT).show();
            }
        });
        bestProductsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_content, fragment);
                fragmentTransaction.commit();

                Toast.makeText(getContext(), "Best Products", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}