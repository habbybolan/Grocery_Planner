package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeNutritionLikedRecipeInteractorImpl
        extends RecipeNutritionInteractorImpl<LikedRecipe> implements RecipeNutritionContract.InteractorLikedRecipeReadOnly {

    public RecipeNutritionLikedRecipeInteractorImpl(DatabaseAccess databaseAccess) {
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
