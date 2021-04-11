package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IngredientListInteractor {

    /**
     * Delete an ingredient
     * @param ingredient   ingredient to delete
     */
    void deleteIngredient(Ingredient ingredient);

    /**
     * Delete a list of ingredients
     * @param ingredients Ingredients to delete
     */
    void deleteIngredients(List<Ingredient> ingredients);

    /**
     * Fetch the Ingredients in the database
     * @param ingredientObserver    observer to store the retrieved ingredients in
     */
    void fetchIngredients(ObservableArrayList<Ingredient> ingredientObserver) throws ExecutionException, InterruptedException;

    /**
     * Find Ingredient objects based on the ingredientName typed by user.
     * @param ingredientName   The name of the ingredient object to try to retrieve.
     */
    public void searchIngredients(String ingredientName, ObservableArrayList<Ingredient> ingredientObserver) throws ExecutionException, InterruptedException;
}
