package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.http.RestWebService;

import javax.inject.Inject;

public class RecipeDetailsInteractorImpl implements RecipeDetailsContract.Interactor {

    protected DatabaseAccess databaseAccess;
    protected RestWebService restWebService;

    @Inject
    public RecipeDetailsInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService) {
        this.databaseAccess = databaseAccess;
        this.restWebService = restWebService;
    }
}
