package com.habbybolan.groceryplanner.models.secondarymodels;

public class Category {

    public static final String NO_CATEGORY = "No Category";
    long id;
    String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
