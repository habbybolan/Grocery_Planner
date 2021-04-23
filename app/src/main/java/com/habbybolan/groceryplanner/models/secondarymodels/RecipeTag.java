package com.habbybolan.groceryplanner.models.secondarymodels;

public class RecipeTag {

    private long id;
    private String title;

    public RecipeTag(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}
