package com.habbybolan.groceryplanner.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.habbybolan.groceryplanner.models.Ingredient;

@Entity(primaryKeys = {"groceryId", "ingredientName"})
public class GroceryIngredientBridge {
    public long groceryId;
    @NonNull
    @ColumnInfo(index = true)
    public String ingredientName;
    @ColumnInfo(name="is_favorite")
    public boolean isFavorite = false;
    public int quantity;
    @ColumnInfo(name="quantity_type")
    public String quantityType;

    public GroceryIngredientBridge(long groceryId, @NonNull String ingredientName, boolean isFavorite, int quantity, String quantityType) {
        this.groceryId = groceryId;
        this.ingredientName = ingredientName;
        this.isFavorite = isFavorite;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public GroceryIngredientBridge(GroceryEntity groceryEntity, Ingredient ingredient) {
        this.groceryId = groceryEntity.getGroceryId();
        ingredientName = ingredient.getName();
        quantity = ingredient.getQuantity();
        quantityType = ingredient.getQuantityType();
    }
}
