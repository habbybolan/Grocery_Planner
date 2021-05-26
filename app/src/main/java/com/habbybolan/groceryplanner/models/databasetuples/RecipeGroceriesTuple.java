package com.habbybolan.groceryplanner.models.databasetuples;

import java.sql.Timestamp;

/**
 * Room tuple used to display the Grocery lists that are holding a specific, with the amount the recipe was added to the Grocery list
 */
public class RecipeGroceriesTuple {

    public long groceryId;
    public Long onlineGroceryId;
    public String groceryName;
    public Timestamp dateSynchronized;
    public int amount;
}
