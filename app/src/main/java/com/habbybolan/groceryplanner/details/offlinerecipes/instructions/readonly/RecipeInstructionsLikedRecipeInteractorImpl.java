package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeInstructionsLikedRecipeInteractorImpl
        extends RecipeInstructionsInteractorImpl<LikedRecipe> implements RecipeInstructionsContract.InteractorLikedRecipe {

    public RecipeInstructionsLikedRecipeInteractorImpl(DatabaseAccess databaseAccess) {
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
