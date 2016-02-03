package com.app.sample.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sample.shop.ActivityAddresses;
import com.app.sample.shop.ActivityCreditCard;
import com.app.sample.shop.R;
import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by LENOVO on 1/19/2016.
 */
public class ProfileFragment extends Fragment {
    private View view;
    private MaterialRippleLayout addresses, creditCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, null);
        addresses = (MaterialRippleLayout) view.findViewById(R.id.addresses);
        creditCard = (MaterialRippleLayout) view.findViewById(R.id.credit_cards);


        addresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityAddresses.class);
                startActivity(i);
            }
        });

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityCreditCard.class);
                startActivity(i);
            }
        });

        return view;
    }


}
