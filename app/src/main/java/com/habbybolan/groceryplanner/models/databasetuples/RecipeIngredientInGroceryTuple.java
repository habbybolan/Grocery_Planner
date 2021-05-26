package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

/**
 * Room tuple to display Recipe ingredients that are added to the Grocery list through the Recipe.
 */
public class RecipeIngredientInGroceryTuple extends RecipeIngredientsTuple {

    @ColumnInfo(name = "is_checked")
    public boolean isChecked;
    @ColumnInfo(name = "amount")
    public int amount;
    @ColumnInfo(name = "groceryId")
    public long groceryId;
    public Long onlineGroceryId;
}
