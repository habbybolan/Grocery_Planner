package com.habbybolan.groceryplanner.MainPage.recipesnippet;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import javax.inject.Inject;

public class RecipeSnippetInteractorImpl implements RecipeSnippetInteractor {

    private HttpRecipe httpRecipe;
    private DatabaseAccess databaseAccess;

    @Inject
    public RecipeSnippetInteractorImpl(HttpRecipe httpRecipe, DatabaseAccess  databaseAccess) {
        this.httpRecipe = httpRecipe;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void onSaveRecipeToOnline(OnlineRecipe recipe) {

    }

    @Override
    public void onSaveRecipeOffline(OnlineRecipe recipe) {

    }
}
