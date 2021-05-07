package com.habbybolan.groceryplanner.models.webmodels;

import java.util.List;

public class WebServiceResponse<T> {

    private List<T> items;
    private int responseCode;

    public WebServiceResponse(List<T> items, int responseCode) {
        this.items = items;
        this.responseCode = responseCode;
    }

    public WebServiceResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<T> getItems() {
        return items;
    }
    public int getResponseCode() {
        return responseCode;
    }
}
