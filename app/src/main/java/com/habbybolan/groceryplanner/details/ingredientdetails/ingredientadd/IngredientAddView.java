package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public interface IngredientAddView {

    /**
     * Displays all Ingredients.
     */
    void showListOfIngredients(List<Ingredient> ingredients);

    void loadingStarted();
    void loadingFailed(String message);
}
