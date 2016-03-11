package com.app.sample.shop.model;

import java.util.ArrayList;

public class Order {

    private int OrderID;
    private String dateOfOrder;
    private String status;
    private double shippingCost;
    private String deliverDate;
    private double TotalCost;
    private int AdderessID;
    private int ShipperSID;
    private int CustomerID;
    private ArrayList<Order_Products> orderProductses;
    private int shippingTime;

    public int getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(int shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Order(){}
    public Order(int orderID, int customerID, String dateOfOrder, String status, double shippingCost, String deliverDate, double totalCost, int adderessID, int shipperSID, ArrayList<Order_Products> orderProductses) {
        OrderID = orderID;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.shippingCost = shippingCost;
        this.deliverDate = deliverDate;
        TotalCost = totalCost;
        AdderessID = adderessID;
        CustomerID = customerID;
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

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public double getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(double totalCost) {
        TotalCost = totalCost;
    }

    public int getAdderessID() {
        return AdderessID;
    }

    public void setAdderessID(int adderessID) {
        AdderessID = adderessID;
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

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
}
