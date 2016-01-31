package com.app.sample.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.shop.ActivityItemDetails;
import com.app.sample.shop.ActivityOrdersDetails;
import com.app.sample.shop.R;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Product;
import com.balysv.materialripple.MaterialRippleLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOHAMMAD on 1/30/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final int mBackground;
    private List<Order> original_items = new ArrayList<>();
    private final TypedValue mTypedValue = new TypedValue();
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView orderNumber;
        public TextView orderStauts;
        public TextView dateOfOrder;
        public TextView deliverDate;
        public TextView totalCost;
        public MaterialRippleLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            orderNumber = (TextView) v.findViewById(R.id.orderNumber);
            orderStauts = (TextView) v.findViewById(R.id.orderStauts);
            dateOfOrder = (TextView) v.findViewById(R.id.dateOfOrder);
            deliverDate = (TextView) v.findViewById(R.id.deliverDate);
            totalCost = (TextView) v.findViewById(R.id.totalCost);
            lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        }

    }

    public OrderAdapter(Context context, List<Order> items) {
        this.context = context;
        original_items = items;
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);/////////////////////////////////////
        v.setBackgroundResource(mBackground);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Order order = original_items.get(position);
        holder.orderNumber.setText("order Number : "+order.getOrderID());
        holder.orderStauts.setText("Status : " + order.getStatus());
        holder.dateOfOrder.setText("Date Of Order : " + order.getDateOfOrder());
        holder.deliverDate.setText("Deliver In : " + order.getDeliverDate());
        holder.totalCost.setText("Total : $" + order.getTotalCost());
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityOrdersDetails.class);
                Constant.currentOrder = order;
                context.startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return original_items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
