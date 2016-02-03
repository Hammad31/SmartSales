package com.app.sample.shop.model;

import java.io.Serializable;

/**
 * Created by LENOVO on 2/2/2016.
 */
public class CreditCard implements Serializable {
    private int CardID;
    private String type, name, CVVNumber, number;
    private String expireData;

    public CreditCard(int cardID, String type, String name, String CVVNumber, String number, String expireData) {
        CardID = cardID;
        this.type = type;
        this.name = name;
        this.CVVNumber = CVVNumber;
        this.number = number;
        this.expireData = expireData;
    }

    public CreditCard(){

    }

    public int getCardID() {
        return CardID;
    }

    public void setCardID(int cardID) {
        CardID = cardID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCVVNumber() {
        return CVVNumber;
    }

    public void setCVVNumber(String CVVNumber) {
        this.CVVNumber = CVVNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpireData() {
        return expireData;
    }

    public void setExpireData(String expireData) {
        this.expireData = expireData;
    }
}
