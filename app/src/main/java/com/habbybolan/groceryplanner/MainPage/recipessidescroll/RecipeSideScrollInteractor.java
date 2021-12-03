package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.callbacks.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.concurrent.ExecutionException;

public interface RecipeSideScrollInteractor {

    /**
     * Saves a recipe to the online database.
     * @param recipe Recipe to save to online database.
     */
    void onRecipeSaved(OnlineRecipe recipe);

    /**
     * Fetches amount recipes of type infoType from online database, at an offset.
     *
     * @param infoType The type of recipes to retrieve from database
     * @param offset   The list offset to retrieve the recipes from
     * @param amount   The amount of recipes to retrieve from the fetch
     * @param callback Callback to signal when the recipes are retrieved from web service
     */
    void fetchRecipes(String infoType, int offset, int amount, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
}