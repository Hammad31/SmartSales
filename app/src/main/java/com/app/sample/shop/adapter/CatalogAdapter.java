package com.app.sample.shop.adapter;

import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.support.v7.widget.DefaultItemAnimator;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.util.TypedValue;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Filter;
    import android.widget.Filterable;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.app.sample.shop.ActivityItemDetails;
    import com.app.sample.shop.R;
    import com.app.sample.shop.data.Constant;
    import com.app.sample.shop.model.Catalog;
    import com.app.sample.shop.model.ItemModel;
    import com.app.sample.shop.model.Product;
    import com.app.sample.shop.widget.DividerItemDecoration;
    import com.app.sample.shop.widget.RoundedTransformation;
    import com.balysv.materialripple.MaterialRippleLayout;
    import com.nostra13.universalimageloader.core.ImageLoader;
    import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
    import com.squareup.picasso.Picasso;

    import java.io.Serializable;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;

/**
 * Created by MOHAMMAD on 1/22/2016.
 */
    public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder>  {

        private final int mBackground;
        private List<Catalog> original_items = new ArrayList<>();

        private final TypedValue mTypedValue = new TypedValue();

        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView title;
            public RecyclerView recyclerView;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.catalogNAME);
                recyclerView = (RecyclerView) v.findViewById(R.id.product_recyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL_LIST));
            }

        }



        // Provide a suitable constructor (depends on the kind of dataset)
        public CatalogAdapter(Context context, ArrayList<Catalog> items) {
            this.context = context;
            original_items = items;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        @Override
        public CatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_raw, parent, false);
            v.setBackgroundResource(mBackground);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Catalog c = original_items.get(position);
            holder.title.setText(c.getCatalogName());
            ProductGridAdapter mAdapter = new ProductGridAdapter(context, c.getProduct());
            holder.recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            holder.recyclerView.invalidate();

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
