package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.sync.SyncRecipeFromResponse;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeDetailsLikedRecipeInteractorImpl implements RecipeDetailsContract.InteractorLikedRecipe {

    private RestWebService restWebService;
    private DatabaseAccess databaseAccess;
    private SyncRecipeFromResponse syncRecipes;

    @Inject
    public RecipeDetailsLikedRecipeInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService) {
        syncRecipes = new SyncRecipeFromResponse(databaseAccess);
        this.databaseAccess = databaseAccess;
        this.restWebService = restWebService;
    }

    @Override
    public void fetchLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullLikedRecipe(recipeId, callback);
    }
}
