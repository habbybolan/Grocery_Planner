package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

public class RecipeIngredientsWithGroceryTuple extends RecipeIngredientsTuple {

    @ColumnInfo(name = "groceryId")
    public long groceryId;
}
