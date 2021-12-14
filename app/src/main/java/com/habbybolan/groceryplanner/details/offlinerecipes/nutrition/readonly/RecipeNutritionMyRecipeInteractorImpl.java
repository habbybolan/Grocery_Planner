package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeNutritionMyRecipeInteractorImpl
        extends RecipeNutritionInteractorImpl<MyRecipe> implements RecipeNutritionContract.InteractorMyRecipeReadOnly {

    public RecipeNutritionMyRecipeInteractorImpl(DatabaseAccess databaseAccess) {
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
