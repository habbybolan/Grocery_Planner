package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractorImpl;

public class RecipeInstructionsInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeInstructionsInteractor {

    public RecipeInstructionsInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

}
