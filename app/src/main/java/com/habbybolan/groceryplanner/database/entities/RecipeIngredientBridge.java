package com.habbybolan.groceryplanner.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

@Entity(primaryKeys = {"recipeId", "ingredientId"})
public class RecipeIngredientBridge {

    @ColumnInfo(index = true)
    public long recipeId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public int quantity;
    @ColumnInfo(name="quantity_type")
    public String quantityType;
    @Embedded
    public SectionEntity sectionEntity;

    public RecipeIngredientBridge(long recipeId, long ingredientId, int quantity, String quantityType) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public RecipeIngredientBridge(RecipeEntity recipeEntity, Ingredient ingredient) {
        recipeId = recipeEntity.getRecipeId();
        ingredientId = ingredient.getId();
        quantity = ingredient.getQuantity();
        quantityType = ingredient.getQuantityType();
    }

    public void setSectionEntity(SectionEntity sectionEntity) {
        this.sectionEntity = sectionEntity;
    }

}
