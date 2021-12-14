package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

public interface IngredientAddPresenter {

    void setView(IngredientAddView view);
    void destroy();
    /**
     * Fetch the Ingredients not inside the current IngredientHolder. Filter using search string.
     * @param ingredientHolder      Holds the Ingredients to not display
     * @param search                Ingredient name search to find similar ingredients stored in database.
     *                              If String is empty, then find all ingredients not in the ingredientHolder
     */
    void fetchIngredientsNotInIngredientHolder(OfflineIngredientHolder ingredientHolder, String search);

    /**
     * Called to display the loaded Ingredients that are not inside the IngredientHolder.
     */
    void displayExistingIngredients();

    /**
     * Add the ingredient to the IngredientHolder by Ingredient
     */
    void addIngredientToIngredientHolder(Ingredient ingredient, OfflineIngredientHolder ingredientHolder);

    /**
     * Add the ingredient to the IngredientHolder by name
     */
    void addIngredientToIngredientHolder(String ingredientName, OfflineIngredientHolder ingredientHolder);
}
