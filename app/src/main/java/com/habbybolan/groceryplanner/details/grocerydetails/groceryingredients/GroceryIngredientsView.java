package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;

public interface GroceryIngredientsView extends ListViewInterface<GroceryIngredient> {

    /**
     * Called when an item's checklist box is selected from the adapter.
     * @param groceryIngredient     The Ingredient whose checklist box was clicked
     */
    void onChecklistSelected(GroceryIngredient groceryIngredient);

    /**
     *
     * @param ingredients
     * @param ingredientNames
     * @param grocery
     */
    //void display(List<GroceryIngredient> ingredients, String[] ingredientNames, Grocery grocery);
}
