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

import com.app.sample.shop.ActivityOrdersDetails;
import com.app.sample.shop.R;
import com.app.sample.shop.data.Constant;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Order_Products;
import com.app.sample.shop.model.Product;
import com.balysv.materialripple.MaterialRippleLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by MOHAMMAD on 1/31/2016.
 */
public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ViewHolder> {

    private final int mBackground;
    private List<Order_Products> orderProductses = new ArrayList<>();
    private final TypedValue mTypedValue = new TypedValue();
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView category;
        public TextView price;
        public TextView total;
        public ImageView image;
        public MaterialRippleLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            category = (TextView) v.findViewById(R.id.category);
            price = (TextView) v.findViewById(R.id.price);
            total = (TextView) v.findViewById(R.id.total);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        }

    }

    public OrderProductsAdapter(Context context, List<Order_Products> items) {
        this.context = context;
        orderProductses = items;
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }


    @Override
    public OrderProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderproducts_row, parent, false);/////////////////////////////////////
        v.setBackgroundResource(mBackground);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Order_Products order_products = orderProductses.get(position);
        final Product p = order_products.getProduct();
        holder.title.setText(p.getName());
        holder.category.setText(p.getType());
        holder.total.setText(order_products.getQuantity() + " X");
        holder.price.setText("$"+p.getPrice());
        if (p.getPhoto().get(0).startsWith("http"))
            ImageLoader.getInstance().displayImage(p.getPhoto().get(0), holder.image);
        else
            ImageLoader.getInstance().displayImage("http://hamoha.com/Project/Image/" + p.getPhoto().get(0), holder.image);


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return orderProductses.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
