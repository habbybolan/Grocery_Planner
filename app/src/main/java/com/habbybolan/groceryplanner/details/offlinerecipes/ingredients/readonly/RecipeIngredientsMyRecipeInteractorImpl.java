package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeIngredientsMyRecipeInteractorImpl
        extends RecipeIngredientsInteractorImpl<MyRecipe> implements RecipeIngredientsContract.InteractorMyRecipe {

    public RecipeIngredientsMyRecipeInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<MyRecipe> callback) {
        try {
            databaseAccess.fetchFullMyRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            callback.onFail("");
        }
    }
}
