package com.app.sample.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.sample.shop.ActivityAddresses;
import com.app.sample.shop.R;
import com.balysv.materialripple.MaterialRippleLayout;
import com.vinaygaba.creditcardview.CreditCardView;

/**
 * Created by LENOVO on 1/19/2016.
 */
public class ProfileFragment extends Fragment {
    private View view;
    private MaterialRippleLayout addresses, creditCard;
    CreditCardView creditCardView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, null);
        addresses = (MaterialRippleLayout) view.findViewById(R.id.addresses);
        creditCard = (MaterialRippleLayout) view.findViewById(R.id.credit_cards);
        creditCardView = (CreditCardView) view.findViewById(R.id.card2);


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
                Toast.makeText(getContext(), creditCardView.getCardNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
