package com.app.sample.shop.model;

import java.util.ArrayList;

/**
 * Created by MOHAMMAD on 1/22/2016.
 */

public class Catalog {

    private String CatalogID;
    private String CatalogName;
    private ArrayList<Product> product;

    public Catalog(String catalogID, String catalogName, ArrayList<Product> product) {
        CatalogID = catalogID;
        CatalogName = catalogName;
        this.product = product;
    }

    public String getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(String catalogID) {
        CatalogID = catalogID;
    }

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }
}
