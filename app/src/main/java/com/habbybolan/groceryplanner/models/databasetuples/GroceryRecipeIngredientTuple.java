package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

public class GroceryRecipeIngredientTuple {

    @ColumnInfo(name = "ingredientId")
    public long ingredientId;
    @ColumnInfo(name = "recipeId")
    public long recipeId;
    @ColumnInfo(name = "groceryId")
    public long groceryId;
    @ColumnInfo(name = "ingredientName")
    public String ingredientName;
    @ColumnInfo(name = "price")
    public int price;
    @ColumnInfo(name = "price_per")
    public int pricePer;
    @ColumnInfo(name = "price_type")
    public String priceType;
    @ColumnInfo(name = "is_checked")
    public boolean isChecked;
    @ColumnInfo(name = "food_type")
    public String foodType;
    @ColumnInfo(name = "recipeQuantity")
    public int recipeQuantity;
    @ColumnInfo(name = "recipeQuantityType")
    public String recipeQuantityType;
    @ColumnInfo(name = "groceryQuantity")
    public int groceryQuantity;
    @ColumnInfo(name = "groceryQuantityType")
    public String groceryQuantityType;
    @ColumnInfo(name = "recipeName")
    public String recipeName;
    @ColumnInfo(name = "amount")
    public int amount;


}
