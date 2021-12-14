package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

import java.util.concurrent.ExecutionException;

public interface IngredientAddInteractor {

    /**
     * Fetch the Ingredients not inside the current IngredientHolder. Filter using search string.
     * @param ingredientHolder      Holds the Ingredients to not display
     * @param search                Ingredient name search to find similar ingredients stored in database.
     *                              If String is empty, then find all ingredients not in the ingredientHolder
     */
    void fetchIngredientsNotInIngredientHolder(DbCallback<Ingredient> callback, OfflineIngredientHolder ingredientHolder, String search) throws ExecutionException, InterruptedException;

    /**
     * Add the ingredient to the IngredientHolder by Ingredient
     * @param ingredient            Ingredient to add to ingredientHolder
     * @param ingredientHolder      All of the ingredients currently inside the ingredientHolder
     * @param callback              callback when ingredient is added
     */
    void addIngredientToIngredientHolder(Ingredient ingredient, OfflineIngredientHolder ingredientHolder, DbSingleCallbackWithFail<Ingredient> callback);

    /**
     * Add the ingredient to the IngredientHolder by name.
     * @param ingredientName        Name of ingredient to add
     * @param ingredientHolder      Holder container of the Ingredients
     * @param callback              callback when ingredient is added
     */
    void addIngredientToIngredientHolder(String ingredientName, OfflineIngredientHolder ingredientHolder, DbSingleCallbackWithFail<Ingredient> callback);
}
