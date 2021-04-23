package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public interface IngredientAddInteractor {

    /**
     * Fetch the Ingredients not inside the current IngredientHolder.
     * @param callback              callback for updating the ingredients fetched
     * @param ingredientHolder      Holds the Ingredients to not display
     */
    void fetchIngredientsNotInIngredientHolder(DbCallback<Ingredient> callback, IngredientHolder ingredientHolder) throws ExecutionException, InterruptedException;

    /**
     * Add the checked Ingredients to the IngredientHolder
     */
    void addCheckedToIngredientHolder(HashSet<Ingredient> checkedIngredients, IngredientHolder ingredientHolder);
}
