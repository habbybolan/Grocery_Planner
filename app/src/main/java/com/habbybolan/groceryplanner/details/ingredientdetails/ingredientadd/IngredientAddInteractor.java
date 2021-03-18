package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public interface IngredientAddInteractor {

    /**
     * Fetch the Ingredients not inside the current IngredientHolder.
     * @param ingredientHolder      Holds the Ingredients to not display
     */
    void fetchIngredientsNotInIngredientHolder(ObservableArrayList<Ingredient> ingredientsObserver, IngredientHolder ingredientHolder) throws ExecutionException, InterruptedException;

    /**
     * Add the checked Ingredients to the IngredientHolder
     */
    void addCheckedToIngredientHolder(HashSet<Ingredient> checkedIngredients, IngredientHolder ingredientHolder);
}
