package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;

public interface IngredientAddView {

    /**
     * Displays all Ingredients.
     */
    void showListOfIngredients(List<Ingredient> ingredients);

    /**
     * Leave the Ingredient Add fragment and send back the recently inserted ingredient.
     * @param ingredient    Ingredient recently inserted into IngredientHolder
     */
    void leaveFragment(Ingredient ingredient);
}
