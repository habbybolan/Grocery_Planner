package com.habbybolan.groceryplanner.details.recipe;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public abstract class RecipeDetailsInteractorImpl implements RecipeDetailsInteractor{

    protected DatabaseAccess databaseAccess;

    public RecipeDetailsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        databaseAccess.updateRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe.getId());
    }
}
