package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

public class GroceryIngredientsTuple {

    @ColumnInfo(name = "ingredientId")
    public long ingredientId;
    @ColumnInfo(name = "ingredientName")
    public String ingredientName;
    @ColumnInfo(name = "price")
    public float price;
    @ColumnInfo(name = "price_per")
    public int pricePer;
    @ColumnInfo(name = "price_type")
    public String priceType;
    @ColumnInfo(name = "quantity")
    public float quantity;
    @ColumnInfo(name = "quantityMeasId")
    public Long quantityMeasId;
    @ColumnInfo(name = "food_type")
    public String foodType;
    @ColumnInfo(name = "is_checked")
    public boolean isChecked;
}
