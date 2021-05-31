package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

/**
 * Room tuple to display Recipe ingredients with a check if they are added to the Grocery list or not.
 * groceryId will either have an id > 0 if the recipe ingredient exists inside the Grocery, otherwise id = 0.
 */
public class RecipeIngredientsWithGroceryCheckTuple {

    @ColumnInfo(name = "groceryId")
    public long groceryId;
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
}
