package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class RecipeInstructionsEditInteractorImpl extends RecipeInstructionsInteractorImpl implements RecipeInstructionsContract.InteractorEdit {

    public RecipeInstructionsEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void updateRecipe(OfflineRecipe recipe) {
        databaseAccess.updateRecipe(recipe);
    }
}
