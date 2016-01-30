package com.app.sample.shop.model;

/**
 * Created by MOHAMMAD on 1/23/2016.
 */
public class CompanyUnit {

    private String CUName;
    private String CompanyName;
    private String CULocation;
    private String Street;
    private String Civil;
    private String Region;
    private String CUCategory;
    private int CUID;
    private String CUAccountNO;
    private int ComID;

    public CompanyUnit(String CUName, int comID, String CUAccountNO, int CUID, String CUCategory, String region, String civil, String street, String CULocation,String CompanyName) {
        this.CUName = CUName;
        ComID = comID;
        this.CUAccountNO = CUAccountNO;
        this.CUID = CUID;
        this.CUCategory = CUCategory;
        Region = region;
        Civil = civil;
        Street = street;
        this.CULocation = CULocation;
        this.CompanyName = CompanyName;
    }

    public CompanyUnit(CompanyUnit companyUnit) {
        this.CUName = companyUnit.CUName;
        this.ComID = companyUnit.ComID;
        this.CUAccountNO = companyUnit.CUAccountNO;
        this.CUID = companyUnit.CUID;
        this.CUCategory = companyUnit.CUCategory;
        this.Region = companyUnit.Region;
        this.Civil =companyUnit.Civil;
        this.Street = companyUnit.Street;
        this.CULocation = companyUnit.CULocation;
        this.CompanyName = companyUnit.CompanyName;
    }

    public String getCUName() {
        return CUName;
    }

    public void setCUName(String CUName) {
        this.CUName = CUName;
    }

    public String getCUAccountNO() {
        return CUAccountNO;
    }

    public void setCUAccountNO(String CUAccountNO) {
        this.CUAccountNO = CUAccountNO;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public int getCUID() {
        return CUID;
    }

    public void setCUID(int CUID) {
        this.CUID = CUID;
    }

    public String getCUCategory() {
        return CUCategory;
    }

    public void setCUCategory(String CUCategory) {
        this.CUCategory = CUCategory;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getCivil() {
        return Civil;
    }

    public void setCivil(String civil) {
        Civil = civil;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCULocation() {
        return CULocation;
    }

    public void setCULocation(String CULocation) {
        this.CULocation = CULocation;
    }

    public int getComID() {
        return ComID;
    }

    public void setComID(int comID) {
        ComID = comID;
    }
}
