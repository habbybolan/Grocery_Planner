package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

public class RecipeIngredientsTuple {

    @ColumnInfo(name = "recipeId")
    public long recipeId;
    @ColumnInfo(name = "recipeName")
    public String recipeName;
    @ColumnInfo(name = "ingredientId")
    public long ingredientId;
    @ColumnInfo(name = "ingredientName")
    public String ingredientName;
    public float price;
    @ColumnInfo(name = "price_per")
    public int pricePer;
    @ColumnInfo(name = "price_type")
    public String priceType;
    @ColumnInfo(name = "food_type")
    public String foodType;
    @ColumnInfo(name = "quantity")
    public float quantity;
    @ColumnInfo(name = "quantityMeasId")
    public Long quantityMeasId;
}