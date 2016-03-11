package com.app.sample.shop.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.app.sample.shop.ActicityCheckout;
import com.app.sample.shop.R;
import com.app.sample.shop.adapter.CartListAdapter;
import com.app.sample.shop.data.GlobalVariable;
import com.app.sample.shop.data.SessionManager;
import com.app.sample.shop.model.Cart_Product;
import com.app.sample.shop.model.Product;
import com.app.sample.shop.model.QuantityChecker;
import com.app.sample.shop.widget.DividerItemDecoration;

public class CartFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GlobalVariable global;
    private CartListAdapter mAdapter;
    private TextView item_total, price_total;
    private LinearLayout lyt_notfound;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        view = inflater.inflate(R.layout.fragment_cart, null);
        global = (GlobalVariable) getActivity().getApplication();
        item_total = (TextView) view.findViewById(R.id.item_total);
        price_total = (TextView) view.findViewById(R.id.price_total);
        lyt_notfound = (LinearLayout) view.findViewById(R.id.lyt_notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //set data and list adapter
        mAdapter = new CartListAdapter(getActivity(), global.getCart());
        recyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new CartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Product obj) {
                dialogCartAction(obj, position);
            }

            //dialogCartAction(obj, position);
        });
        ((Button) view.findViewById(R.id.bt_checkout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getItemCount() != 0) {
                    checkoutConfirmation();
                }
            }
        });
        setTotalPrice();
        if (mAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }
        return view;
    }

    private void setTotalPrice() {
        item_total.setText(" - " + global.getCartItemTotal() + " Items");
        price_total.setText(" $ " + global.getCartPriceTotal());
    }

    private void dialogCartAction(final Product model, final int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cart_option);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((TextView) dialog.findViewById(R.id.title)).setText(model.getName());
        final TextView qty = (TextView) dialog.findViewById(R.id.quantity);
        Cart_Product cart_product = (Cart_Product) new Select().from(Cart_Product.class).where("ProductID = ?", model.getPID()).execute().get(0);
        qty.setText(cart_product.Quantity + "");
        if (cart_product.Quantity <= 1) {
            ImageView a = (ImageView) dialog.findViewById(R.id.img_decrease);
            a.setVisibility(View.INVISIBLE);
        }
        ((ImageView) dialog.findViewById(R.id.img_decrease)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart_Product cart_product = (Cart_Product) new Select().from(Cart_Product.class).where("ProductID = ?", model.getPID()).execute().get(0);
                if (cart_product.Quantity > 1) {
                    cart_product.Quantity--;
                    cart_product.save();
                    qty.setText(cart_product.Quantity + "");
                    if (cart_product.Quantity == 1) {
                        ImageView a = (ImageView) dialog.findViewById(R.id.img_decrease);
                        a.setVisibility(View.INVISIBLE);
                    }

                } else if (cart_product.Quantity <= 1) {
                    ImageView a = (ImageView) dialog.findViewById(R.id.img_decrease);
                    a.setVisibility(View.INVISIBLE);
                    //if (AreYouSure()) {
                    //  cart_product.delete();
                    //global.removeCart(model);
                    //}
                }
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
            }

        });
        ((ImageView) dialog.findViewById(R.id.img_increase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart_Product cart_product = (Cart_Product) new Select().from(Cart_Product.class).where("ProductID = ?", model.getPID()).execute().get(0);
                cart_product.Quantity++;
                cart_product.save();
                ImageView a = (ImageView) dialog.findViewById(R.id.img_decrease);
                a.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
                qty.setText(cart_product.Quantity + "");
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //global.updateItemTotal(model);
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.removeCart(model);
                mAdapter.notifyDataSetChanged();
                setTotalPrice();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void checkoutConfirmation() {
        boolean check = CheckInventory();
        if (!check)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Checkout Confirmation");
        builder.setMessage("Are you sure continue to checkout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //global.clearCart();
                //mAdapter.notifyDataSetChanged();
                sessionManager.checkLogin();
                Intent intent = new Intent(view.getContext(), ActicityCheckout.class);
                startActivity(intent);
                Snackbar.make(view, "Checkout success", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private boolean CheckInventory() {
        for (int i = 0; i < global.cart_products.size(); i++) {
            QuantityChecker quantityChecker = new QuantityChecker(global.cart_products.get(i).ProductID, global.cart_products.get(i).Quantity);
            //No available stock...
            if (!quantityChecker.Check()) {
                Toast.makeText(getContext(), global.getCart().get(i).getName() + ", Has no enough quantity", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "You can only buy " + quantityChecker.GetStockQuantity(), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

}
