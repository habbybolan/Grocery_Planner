package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

import java.sql.Timestamp;

/**
 * Room tuple to display an Ingredient and the recipe it belongs to.
 */
public class RecipeIngredientsTuple {

    @ColumnInfo(name = "recipeId")
    public long recipeId;
    public Long onlineRecipeId;
    @ColumnInfo(name = "recipeName")
    public String recipeName;
    @ColumnInfo(name = "ingredientId")
    public long ingredientId;
    public Long onlineIngredientId;
    @ColumnInfo(name = "ingredientName")
    public String ingredientName;
    @ColumnInfo(name = "food_type_id")
    public long foodTypeId;
    @ColumnInfo(name = "quantity")
    public float quantity;
    @ColumnInfo(name = "quantityMeasId")
    public Long quantityMeasId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
}
