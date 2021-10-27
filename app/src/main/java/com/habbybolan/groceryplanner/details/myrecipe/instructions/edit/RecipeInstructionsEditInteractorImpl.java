package com.habbybolan.groceryplanner.details.myrecipe.instructions.edit;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeInstructionsEditInteractorImpl extends RecipeInstructionsInteractorImpl implements RecipeInstructionsContract.InteractorEdit {

    public RecipeInstructionsEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void updateRecipe(MyRecipe myRecipe) {
        databaseAccess.updateMyRecipe(myRecipe);
    }
}
