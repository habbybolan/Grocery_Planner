package com.habbybolan.groceryplanner.database.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.habbybolan.groceryplanner.models.Ingredient;

@Entity(primaryKeys = {"recipeId", "ingredientName"})
public class RecipeIngredientBridge {

    public long recipeId;
    @NonNull
    @ColumnInfo(index = true)
    public String ingredientName;
    public int quantity;
    @ColumnInfo(name="quantity_type")
    public String quantityType;
    @Embedded
    public SectionEntity sectionEntity;

    public RecipeIngredientBridge(long recipeId, @NonNull String ingredientName, int quantity, String quantityType) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public RecipeIngredientBridge(RecipeEntity recipeEntity, Ingredient ingredient) {
        recipeId = recipeEntity.getRecipeId();
        ingredientName = ingredient.getName();
        quantity = ingredient.getQuantity();
        quantityType = ingredient.getQuantityType();
    }

    public void setSectionEntity(SectionEntity sectionEntity) {
        this.sectionEntity = sectionEntity;
    }

}
