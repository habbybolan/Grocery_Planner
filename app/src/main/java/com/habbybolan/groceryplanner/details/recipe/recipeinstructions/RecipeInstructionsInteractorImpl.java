package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeInstructionsInteractorImpl implements RecipeInstructionsContract.Interactor {

    private DatabaseAccess databaseAccess;

    @Inject
    public RecipeInstructionsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        databaseAccess.updateRecipe(offlineRecipe);
    }
}
