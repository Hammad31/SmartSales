package com.app.sample.shop.model;

/**
 * Created by MOHAMMAD on 1/30/2016.
 */
public class Order_Products {

    private int OrderID;
    private int ProductID;
    private int quantity;
    private Product product;

    public Order_Products(int orderID, int productID, int quantity, Product product) {
        OrderID = orderID;
        ProductID = productID;
        this.quantity = quantity;
        this.product = product;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
