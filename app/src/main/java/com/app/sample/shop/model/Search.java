package com.app.sample.shop.model;

/**
 * Created by MOHAMMAD on 2/5/2016.
 */
public class Search {
    private String SearchText;
    private String SearchCatalog;
    private String SearchBrand;
    private String SearchLocation;
    private String SearchPrice;
    private String SearchSort;



    public Search() {
    }

    public Search(String searchText, String searchCatalog, String searchBrand, String searchLocation, String searchPrice, String searchSort) {
        SearchText = searchText;
        SearchCatalog = searchCatalog;
        SearchBrand = searchBrand;
        SearchLocation = searchLocation;
        SearchPrice = searchPrice;
        SearchSort = searchSort;
    }

    public String getSearchText() {
        return SearchText;
    }

    public void setSearchText(String searchText) {
        SearchText = searchText;
    }

    public String getSearchCatalog() {
        return SearchCatalog;
    }

    public void setSearchCatalog(String searchCatalog) {
        SearchCatalog = searchCatalog;
    }

    public String getSearchBrand() {
        return SearchBrand;
    }

    public void setSearchBrand(String searchBrand) {
        SearchBrand = searchBrand;
    }

    public String getSearchLocation() {
        return SearchLocation;
    }

    public void setSearchLocation(String searchLocation) {
        SearchLocation = searchLocation;
    }

    public String getSearchPrice() {
        return SearchPrice;
    }

    public void setSearchPrice(String searchPrice) {
        SearchPrice = searchPrice;
    }

    public String getSearchSort() {
        return SearchSort;
    }

    public void setSearchSort(String searchSort) {
        SearchSort = searchSort;
    }
}
