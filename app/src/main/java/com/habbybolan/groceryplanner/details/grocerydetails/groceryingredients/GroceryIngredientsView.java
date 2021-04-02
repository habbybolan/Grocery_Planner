package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;

public interface GroceryIngredientsView extends ListViewInterface<GroceryIngredient> {

    void onChecklistSelected(GroceryIngredient groceryIngredient);
}
