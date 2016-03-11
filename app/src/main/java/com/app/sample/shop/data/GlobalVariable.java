package com.app.sample.shop.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.app.sample.shop.R;
import com.app.sample.shop.model.Cart_Product;
import com.app.sample.shop.model.Product;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends com.activeandroid.app.Application {
    public List<Cart_Product> cart_products = new ArrayList<>();
    public List<Product> cart = new ArrayList<>();
    private Tracker mTracker;
    public PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("AT6nu0TQYqNe7-yq8WdGZw24yere5qEcjmzE_gnBVblp1N5LE4B_WkVS_Lblg9vXBKVljH2corTLmUqf");

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }

    public void addCart(Product model, Cart_Product cart_product) {
        cart.add(model);
        cart_products.add(cart_product);

    }

    public void SaveToDatabase(Product model, int quantity) {
        Cart_Product cart_product = new Cart_Product();
        cart_product.Quantity = quantity;
        cart_product.ProductID = model.getPID();
        cart_product.save();
    }

    public void DeleteFromDatabase(Product model) {
        Cart_Product cart_product = (Cart_Product) new Select().from(Cart_Product.class).where("ProductID = ?", model.getPID()).execute().get(0);
        Cart_Product.delete(Cart_Product.class, cart_product.getId());
    }

    public void removeCart(Product model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getPID() == model.getPID()) {
                Cart_Product cart_product = (Cart_Product) new Select().from(Cart_Product.class).where("ProductID = ?", cart.get(i).getPID()).execute().get(0);
                cart_product.delete();
                cart_products.remove(i);
                cart.remove(i);
                break;
            }
        }
    }

    public void clearCart() {
        new Delete().from(Cart_Product.class).where("ProductID > ?", 0).execute();
        cart.clear();
        cart_products.clear();
    }

    public List<Product> getCart() {
        return cart;
    }

    public double getCartPriceTotal() {
        double total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + (cart.get(i).getPrice() * cart_products.get(i).Quantity);
        }
        return total;
    }

    public int getCartItemTotal() {
        int total = 0;
        for (int i = 0; i < cart_products.size(); i++)
            total = total + cart_products.get(i).Quantity;
        return total;
    }

/*
    public void updateItemTotal(Product model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId() == model.getId()) {
                cart.remove(i);
                cart.add(i, model);
                break;
            }
        }
    }
*/

    public boolean isCartExist(Product model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getPID() == model.getPID()) {
                return true;
            }
        }
        return false;
    }
}