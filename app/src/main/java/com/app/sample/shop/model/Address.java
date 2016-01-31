package com.app.sample.shop.model;

import java.io.Serializable;

/**
 * Created by LENOVO on 1/29/2016.
 */
public class Address implements Serializable {
    private int AID;
    private String country, city, region, street, building;
    private double latitude, longitude;

    public Address(int AID, String country, String city, String region, String street, String building, double latitude, double longitude) {
        this.AID = AID;
        this.country = country;
        this.city = city;
        this.region = region;
        this.street = street;
        this.building = building;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Address() {
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
