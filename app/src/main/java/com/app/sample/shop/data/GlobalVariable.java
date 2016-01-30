package com.app.sample.shop.data;

import android.app.Application;

import com.app.sample.shop.model.Product;
import com.app.sample.shop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application {
    private List<Product> cart = new ArrayList<>();

    public void addCart(Product model) {
        cart.add(model);
    }

    public void removeCart(Product model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getPID() == model.getPID()) {
                cart.remove(i);
                break;
            }
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public List<Product> getCart() {
        return cart;
    }

    public long getCartPriceTotal() {
        long total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + cart.get(i).getPrice();
        }
        return total;
    }

    public int getCartItemTotal() {
        return cart.size();
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