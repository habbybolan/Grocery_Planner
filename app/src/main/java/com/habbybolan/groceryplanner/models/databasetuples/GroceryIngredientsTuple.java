package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

/**
 * Room Tuple for querying the Ingredient within a Grocery, including if the ingredient is checked or not.
 */
public class GroceryIngredientsTuple {

    @ColumnInfo(name = "ingredientId")
    public long ingredientId;
    public Long onlineIngredientId;
    @ColumnInfo(name = "ingredientName")
    public String ingredientName;
    @ColumnInfo(name = "quantity")
    public float quantity;
    @ColumnInfo(name = "quantityMeasId")
    public Long quantityMeasId;
    @ColumnInfo(name = "food_type")
    public String foodType;
    @ColumnInfo(name = "is_checked")
    public boolean isChecked;
}
