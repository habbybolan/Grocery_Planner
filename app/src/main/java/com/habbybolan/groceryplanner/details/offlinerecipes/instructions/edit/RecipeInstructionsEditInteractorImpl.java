package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeInstructionsEditInteractorImpl
        extends RecipeInstructionsInteractorImpl<MyRecipe> implements RecipeInstructionsContract.InteractorEdit {

    public RecipeInstructionsEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void updateRecipe(OfflineRecipe recipe) {
        databaseAccess.updateRecipe(recipe);
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
