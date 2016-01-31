package com.app.sample.shop.model;

import java.util.ArrayList;

/**
 * Created by MOHAMMAD on 1/30/2016.
 */
public class Order {

    private int OrderID;
    private String dateOfOrder;
    private String status;
    private int shippingCost;
    private String deliverDate;
    private int TotalCost;
    private int AdderessID;
    private int PaymentID;
    private int ShipperSID;
    private ArrayList<Order_Products> orderProductses;

    public Order(int orderID, String dateOfOrder, String status, int shippingCost, String deliverDate, int totalCost, int adderessID, int paymentID, int shipperSID, ArrayList<Order_Products> orderProductses) {
        OrderID = orderID;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.shippingCost = shippingCost;
        this.deliverDate = deliverDate;
        TotalCost = totalCost;
        AdderessID = adderessID;
        PaymentID = paymentID;
        ShipperSID = shipperSID;
        this.orderProductses = orderProductses;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public int getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(int totalCost) {
        TotalCost = totalCost;
    }

    public int getAdderessID() {
        return AdderessID;
    }

    public void setAdderessID(int adderessID) {
        AdderessID = adderessID;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    public int getShipperSID() {
        return ShipperSID;
    }

    public void setShipperSID(int shipperSID) {
        ShipperSID = shipperSID;
    }

    public ArrayList<Order_Products> getOrderProductses() {
        return orderProductses;
    }

    public void setOrderProductses(ArrayList<Order_Products> orderProductses) {
        this.orderProductses = orderProductses;
    }
}
