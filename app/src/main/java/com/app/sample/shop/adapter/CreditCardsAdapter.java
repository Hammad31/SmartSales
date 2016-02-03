package com.app.sample.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.sample.shop.ActivityCreditCard;
import com.app.sample.shop.R;
import com.app.sample.shop.model.CreditCard;
import com.vinaygaba.creditcardview.CardNumberFormat;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2/2/2016.
 */
public class CreditCardsAdapter extends RecyclerView.Adapter<CreditCardsAdapter.ViewHolder> {
    private List<CreditCard> creditCardList = new ArrayList();
    private Context context;
    private ActivityCreditCard activityCreditCard;
    public LinearLayout crditCardsLayout, noCreditCards;

    public CreditCardsAdapter(Context context, List creditCardList, ActivityCreditCard activityCreditCard) {
        this.activityCreditCard = activityCreditCard;
        this.context = context;
        this.creditCardList = creditCardList;
        this.crditCardsLayout = (LinearLayout) activityCreditCard.findViewById(R.id.cards_layout);
        this.noCreditCards = (LinearLayout) activityCreditCard.findViewById(R.id.no_cards);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.credit_card_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void swapItems(List<CreditCard> items) {
        //Update the data set
        this.creditCardList = items;
        notifyDataSetChanged();
    }

    public void updateView() {
        //If empty will show new linear layout with image and text view
        //To let the user know that he has no cards
        if (creditCardList.size() <= 0) {
            crditCardsLayout.setVisibility(View.GONE);
            noCreditCards.setVisibility(View.VISIBLE);
        } else {
            //If the user has card, new or old one..
            //The crditCardsLayout will come and noCreditCards will go
            crditCardsLayout.setVisibility(View.VISIBLE);
            noCreditCards.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CreditCard creditCard = creditCardList.get(position);
        holder.creditCardView.setType(CardType.AUTO);
        holder.creditCardView.setIsEditable(false);
        holder.creditCardView.setCardName(creditCard.getName().toUpperCase());
        holder.creditCardView.setCardNumber(creditCard.getNumber());
        holder.creditCardView.setIsCardNameEditable(false);
        holder.creditCardView.setIsCardNumberEditable(false);
        holder.creditCardView.setIsExpiryDateEditable(false);
        holder.creditCardView.setExpiryDate(creditCard.getExpireData());
        holder.creditCardView.setCardNumberFormat(CardNumberFormat.ALL_DIGITS);

        final int pos = position;
        holder.creditCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //AlertDialog is yes/no dialog
                //If the user click yes, the card will deleted
                //No, Card will not be deleted
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activityCreditCard);
                builder1.setMessage("Are you sure that you want to delete your card?");
                builder1.setCancelable(true);
                builder1.setTitle("Delete Credit Card");

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Remove it from the local data set
                                creditCardList.remove(pos);
                                //Remove it from the server
                                //API Here to delete card by id
                                notifyDataSetChanged();
                                updateView();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(context, "Credit Card did not deleted", Toast.LENGTH_SHORT).show();
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
        return creditCardList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public CreditCardView creditCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            creditCardView = (CreditCardView) itemView.findViewById(R.id.card);
        }
    }

}
