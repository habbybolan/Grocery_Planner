package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

public interface IngredientAddPresenter {

    void setView(IngredientAddView view);
    void destroy();
    /**
     * Fetch the Ingredients not inside the current IngredientHolder.
     * @param ingredientHolder      Holds the Ingredients to not display
     */
    void fetchIngredientsNotInIngredientHolder(IngredientHolder ingredientHolder);

    /**
     * Called to display the loaded Ingredients that are not inside the IngredientHolder.
     */
    void displayIngredientsToAdd();

    /**
     * Add the checked Ingredients to the IngredientHolder
     */
    void addCheckedToIngredientHolder(IngredientHolder ingredientHolder);

    /**
     * Called when an Ingredient is selected.
     * @param ingredient    Selected Ingredient
     */
    void selectIngredient(Ingredient ingredient);

    /**
     * Called when an Ingredient is un-selected
     * @param ingredient    The unselected Ingredient
     */
    void unSelectIngredient(Ingredient ingredient);
}
