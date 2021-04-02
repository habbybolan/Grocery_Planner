package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"groceryId", "recipeId"})
public class GroceryRecipeBridge {

    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long recipeId;
    public int amount;

    public GroceryRecipeBridge(long groceryId, long recipeId, int amount) {
        this.groceryId = groceryId;
        this.recipeId = recipeId;
        this.amount = amount;
    }
}
