package com.app.sample.shop.model;

/**
 * Created by LENOVO on 2/13/2016.
 */
public class Shipper {
    private String SID;
    private String UserID;
    private String SAccount;
    private String UName;
    private double ShippingPrice;
    private int ShippingTime;
    private int Active;

    public Shipper(String SID, String userID, String SAccount, String UName, double shippingPrice, int shippingTime, int active) {
        this.SID = SID;
        UserID = userID;
        this.SAccount = SAccount;
        this.UName = UName;
        ShippingPrice = shippingPrice;
        ShippingTime = shippingTime;
        Active = active;
    }

    public Shipper() {

    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getSAccount() {
        return SAccount;
    }

    public void setSAccount(String SAccount) {
        this.SAccount = SAccount;
    }

    public double getShippingPrice() {
        return ShippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        ShippingPrice = shippingPrice;
    }

    public int getShippingTime() {
        return ShippingTime;
    }

    public void setShippingTime(int shippingTime) {
        ShippingTime = shippingTime;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }
}
