package com.habbybolan.groceryplanner.details.offlinerecipes.instructions;

import com.habbybolan.groceryplanner.database.DatabaseAccess;

import javax.inject.Inject;

public class RecipeInstructionsInteractorImpl implements RecipeInstructionsContract.Interactor {

    protected DatabaseAccess databaseAccess;

    @Inject
    public RecipeInstructionsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
}
