package com.app.sample.shop.model;

import java.util.ArrayList;

/**
 * Created by MOHAMMAD on 1/23/2016.
 */
public class Company {

    private String comName;
    private String Active;
    private int UserID;
    private int ComID;
    private String CAccountNO;
    private ArrayList<CompanyUnit> companyUnits;


    public Company(String active, String comName, int comID, String CAccountNO, ArrayList<CompanyUnit> companyUnits, int userID) {
        Active = active;
        this.comName = comName;
        ComID = comID;
        this.CAccountNO = CAccountNO;
        this.companyUnits = companyUnits;
        UserID = userID;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public ArrayList<CompanyUnit> getCompanyUnits() {
        return companyUnits;
    }

    public void setCompanyUnits(ArrayList<CompanyUnit> companyUnits) {
        this.companyUnits = companyUnits;
    }

    public String getCAccountNO() {
        return CAccountNO;
    }

    public void setCAccountNO(String CAccountNO) {
        this.CAccountNO = CAccountNO;
    }

    public int getComID() {
        return ComID;
    }

    public void setComID(int comID) {
        ComID = comID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }
}
