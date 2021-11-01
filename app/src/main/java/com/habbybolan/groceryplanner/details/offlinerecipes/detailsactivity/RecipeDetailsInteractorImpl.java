package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeDetailsInteractorImpl implements RecipeDetailsContract.Interactor {

    private DatabaseAccess databaseAccess;
    private RestWebService restWebService;

    @Inject
    public RecipeDetailsInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService) {
        this.databaseAccess = databaseAccess;
        this.restWebService = restWebService;
    }

    @Override
    public void fetchMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullMyRecipe(recipeId, callback);
    }

    @Override
    public void fetchLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullLikedRecipe(recipeId, callback);
    }
}
