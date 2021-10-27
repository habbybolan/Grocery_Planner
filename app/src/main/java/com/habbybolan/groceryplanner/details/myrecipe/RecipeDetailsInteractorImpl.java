package com.habbybolan.groceryplanner.details.myrecipe;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public abstract class RecipeDetailsInteractorImpl implements RecipeDetailsInteractor{

    protected DatabaseAccess databaseAccess;

    public RecipeDetailsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateRecipe(MyRecipe myRecipe) {
        databaseAccess.updateMyRecipe(myRecipe);
    }

    @Override
    public void deleteRecipe(MyRecipe myRecipe) {
        databaseAccess.deleteRecipe(myRecipe.getId());
    }
}
