package com.habbybolan.groceryplanner.models.combinedmodels;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import java.sql.Timestamp;

/*
Grocery with functionality to hold the amount that a recipe is held within the grocery list.
Used to display the Grocery lists associated with a Recipe and the number of times
the recipe was added to the grocery list.
 */
public class GroceryRecipe extends Grocery {

    private int amount;

    public GroceryRecipe(String name, long id, Long onlineId, int amount, Timestamp dateSynchronized) {
        super(name, id, onlineId, dateSynchronized);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
