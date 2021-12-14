package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeInstructionsMyRecipeInteractorImpl
        extends RecipeInstructionsInteractorImpl<MyRecipe> implements RecipeInstructionsContract.InteractorMyRecipe {

    public RecipeInstructionsMyRecipeInteractorImpl(DatabaseAccess databaseAccess) {
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
