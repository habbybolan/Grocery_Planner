package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeOverviewLikedRecipeInteractorImpl
        extends RecipeOverviewInteractorImpl<LikedRecipe>
        implements RecipeOverviewContract.InteractorLikedRecipeReadOnly {

    public RecipeOverviewLikedRecipeInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<LikedRecipe> callback) {
        try {
            databaseAccess.fetchFullLikedRecipe(recipeId, callback);
        }catch (InterruptedException | ExecutionException e) {
            callback.onFail("");
        }
    }
}
