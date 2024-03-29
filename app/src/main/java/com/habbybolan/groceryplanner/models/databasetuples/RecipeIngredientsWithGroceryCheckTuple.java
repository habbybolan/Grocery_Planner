package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

import java.sql.Timestamp;

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
    @ColumnInfo(name = "quantity_meas_id")
    public Long quantityMeasId;
    @ColumnInfo(name = "date_updated")
    public Timestamp dateUpdated;
    @ColumnInfo(name = "date_synchronized")
    public Timestamp dateSynchronized;
}
