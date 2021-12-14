package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeOverviewMyRecipeInteractorImpl extends RecipeOverviewInteractorImpl<MyRecipe>
        implements RecipeOverviewContract.InteractorMyRecipeReadOnly {

    public RecipeOverviewMyRecipeInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<MyRecipe> callback) {
        try {
            databaseAccess.fetchFullMyRecipe(recipeId, callback);
        }catch (InterruptedException | ExecutionException e) {
            callback.onFail("");
        }
    }
}
