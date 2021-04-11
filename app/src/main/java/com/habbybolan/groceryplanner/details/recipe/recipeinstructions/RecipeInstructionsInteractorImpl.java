package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public class RecipeInstructionsInteractorImpl implements RecipeInstructionsInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeInstructionsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe.getId());
    }
}
