package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"groceryId", "ingredientId"})
public class GroceryIngredientEntity {

    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public int quantity;
    @ColumnInfo(name = "quantity_type")
    public String quantityType;

    public GroceryIngredientEntity(long groceryId, long ingredientId, int quantity, String quantityType) {
        this.groceryId = groceryId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }
}
