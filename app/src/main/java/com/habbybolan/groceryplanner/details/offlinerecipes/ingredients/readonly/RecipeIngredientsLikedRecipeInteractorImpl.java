package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeIngredientsLikedRecipeInteractorImpl
        extends RecipeIngredientsInteractorImpl<LikedRecipe> implements RecipeIngredientsContract.InteractorLikedRecipe {

    public RecipeIngredientsLikedRecipeInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<LikedRecipe> callback) {
        try {
            databaseAccess.fetchFullLikedRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            callback.onFail("");
        }
    }
}
