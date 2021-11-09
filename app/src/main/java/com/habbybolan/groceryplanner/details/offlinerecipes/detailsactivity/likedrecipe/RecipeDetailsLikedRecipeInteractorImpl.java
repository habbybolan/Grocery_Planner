package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

public class RecipeDetailsLikedRecipeInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeDetailsContract.InteractorLikedRecipe {

    public RecipeDetailsLikedRecipeInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService) {
        super(databaseAccess, restWebService);
    }

    @Override
    public void fetchLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullLikedRecipe(recipeId, callback);
    }
}
