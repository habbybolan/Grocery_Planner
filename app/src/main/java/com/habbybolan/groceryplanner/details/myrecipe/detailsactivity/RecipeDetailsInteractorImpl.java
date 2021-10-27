package com.habbybolan.groceryplanner.details.myrecipe.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.http.RestWebService;
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
    public void fetchRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullMyRecipe(recipeId, callback);
    }
}
