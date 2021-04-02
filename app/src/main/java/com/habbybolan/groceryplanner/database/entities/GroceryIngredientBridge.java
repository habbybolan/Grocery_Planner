package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"groceryId", "ingredientId"})
public class GroceryIngredientBridge {
    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long ingredientId;
    @ColumnInfo(name = "is_checked")
    public boolean isChecked;

    public GroceryIngredientBridge(long groceryId, long ingredientId, boolean isChecked) {
        this.groceryId = groceryId;
        this.ingredientId = ingredientId;
        this.isChecked = isChecked;
    }
}
