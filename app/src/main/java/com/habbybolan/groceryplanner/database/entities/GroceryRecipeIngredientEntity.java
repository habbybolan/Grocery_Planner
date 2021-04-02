package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"groceryId", "recipeId", "ingredientId"})
public class GroceryRecipeIngredientEntity {

    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long recipeId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public int quantity;
    @ColumnInfo(name = "quantity_type")
    public String quantityType;

    public GroceryRecipeIngredientEntity(long groceryId, long ingredientId, long recipeId, int quantity, String quantityType) {
        this.groceryId = groceryId;
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }
}
