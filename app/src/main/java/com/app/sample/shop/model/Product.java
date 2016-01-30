package com.app.sample.shop.model;

import java.sql.Blob;

/**
 * Created by LENOVO on 1/10/2016.
 */
public class Product {
    private String name;
    private String company;
    private String type;
    private String info;
    private String properties;
    private int quantity;
    private int PID;
    private int ComID;
    private int like;
    private int sales;
    private int CATALOGCatalogID;
    private int CUID;
    private String date;
    private int price;
    private Blob image;
    private String photo;



    public Product(String photo, int price, String date, int CUID, int CATALOGCatalogID, int comID, int PID, int quantity, String info, String type, String name,int like,int sales,String properties) {
        this.photo = photo;
        this.price = price;
        this.date = date;
        this.CUID = CUID;
        this.CATALOGCatalogID = CATALOGCatalogID;
        this.ComID = comID;
        this.PID = PID;
        this.quantity = quantity;
        this.info = info;
        this.type = type;
        this.name = name;
        this.like = like;
        this.sales = sales;
        this.properties = properties;
    }

    public Product(String name, String company, String type, Blob image, int price) {
        this.name = name;
        this.company = company;
        this.type = type;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public Blob getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
    public int getCUID() {
        return CUID;
    }

    public void setCUID(int CUID) {
        this.CUID = CUID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCATALOGCatalogID() {
        return CATALOGCatalogID;
    }

    public void setCATALOGCatalogID(int CATALOGCatalogID) {
        this.CATALOGCatalogID = CATALOGCatalogID;
    }

    public int getComID() {
        return ComID;
    }

    public void setComID(int comID) {
        ComID = comID;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Product() {
    }
}
