package com.app.sample.shop.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by LENOVO on 2/8/2016.
 */
public class Cart_Product extends Model {
    @Column(name = "ProductID")
    public int ProductID;

    @Column(name = "Quantity")
    public int Quantity;

    public Cart_Product() {
        super();
    }

    public Cart_Product(int ProductID, int quantity) {
        super();
        Quantity = quantity;
        this.ProductID = ProductID;
    }

}
