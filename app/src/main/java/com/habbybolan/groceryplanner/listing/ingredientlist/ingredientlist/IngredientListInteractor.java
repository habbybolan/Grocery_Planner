package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

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
     * @param callback    callback to update the ingredients fetched
     */
    void fetchIngredients(DbCallback<Ingredient> callback, SortType sortType) throws ExecutionException, InterruptedException;

    /**
     * Find Ingredient objects based on the ingredientName typed by user.
     * @param ingredientName   The name of the ingredient object to try to retrieve.
     * @param callback         callback for updating the ingredient fetched
     */
    public void searchIngredients(String ingredientName, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
}
