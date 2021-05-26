package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

public interface IngredientEditInteractor {

    /**
     * Update/add the Ingredient to the database Ingredient table, depending on if the Ingredient id exists or not.
     * add the IngredientHolder-Ingredient relationship to the Bridge table if it doesn't already exist.
     * @param ingredientHolder      The IngredientHolder where the Ingredient is being added to.
     * @param ingredient            The Ingredient to update/add to the database
     */
    void updateIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
    boolean isNewIngredient(Ingredient ingredient);
    void deleteRelationship(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
}
