package com.habbybolan.groceryplanner.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

@Entity(primaryKeys = {"recipeId", "ingredientId"}, foreignKeys = {
        @ForeignKey(entity = RecipeEntity.class,
                parentColumns = "recipeId",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = IngredientEntity.class,
                parentColumns = "ingredientId",
                childColumns = "ingredientId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "quantity_meas_id")
})
public class  RecipeIngredientBridge {

    @ColumnInfo(index = true)
    public long recipeId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public float quantity;
    @ColumnInfo(name="quantity_meas_id", index = true)
    public Long quantityMeasId;

    public RecipeIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
    }

    public RecipeIngredientBridge(long recipeId, Ingredient ingredient) {
        this.recipeId = recipeId;
        ingredientId = ingredient.getId();
        quantity = ingredient.getQuantity();
        quantityMeasId = ingredient.getQuantityMeasId();
    }

}
