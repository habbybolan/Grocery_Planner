package com.habbybolan.groceryplanner.details.recipe;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeDetailsInteractorImpl implements RecipeDetailsInteractor{

    protected DatabaseAccess databaseAccess;

    public RecipeDetailsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        databaseAccess.updateRecipe(offlineRecipe);
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        databaseAccess.deleteRecipe(offlineRecipe.getId());
    }
}
