package com.habbybolan.groceryplanner.details.offlinerecipes.instructions;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeInstructionsInteractorImpl<T extends OfflineRecipe> implements RecipeInstructionsContract.Interactor<T> {

    protected DatabaseAccess databaseAccess;

    public RecipeInstructionsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
}
