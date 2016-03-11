package com.app.sample.shop.model;

/**
 * Created by LENOVO on 3/9/2016.
 */
public class Payment {
    private int PID;
    private int CardID;
    private String PaymentDate;
    private double amount;
    private int OID;

    public Payment() {
    }

    public Payment(int PID, int cardID, String paymentDate, double amount, int OID) {
        this.PID = PID;
        CardID = cardID;
        PaymentDate = paymentDate;
        this.amount = amount;
        this.OID = OID;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getCardID() {
        return CardID;
    }

    public void setCardID(int cardID) {
        CardID = cardID;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getOID() {
        return OID;
    }

    public void setOID(int OID) {
        this.OID = OID;
    }
}
