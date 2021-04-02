package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

public class RecipeIngredientInGroceryTuple extends RecipeIngredientsTuple {

    @ColumnInfo(name = "is_checked")
    public boolean isChecked;
    @ColumnInfo(name = "amount")
    public int amount;
    @ColumnInfo(name = "groceryId")
    public long groceryId;
}
