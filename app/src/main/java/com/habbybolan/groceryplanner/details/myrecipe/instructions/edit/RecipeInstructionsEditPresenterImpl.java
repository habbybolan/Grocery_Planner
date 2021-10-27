package com.habbybolan.groceryplanner.details.myrecipe.instructions.edit;

import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeInstructionsEditPresenterImpl extends RecipeInstructionsPresenterImpl<RecipeInstructionsContract.InteractorEdit, RecipeInstructionsContract.RecipeInstructionsView> implements RecipeInstructionsContract.PresenterEdit {

    public RecipeInstructionsEditPresenterImpl(RecipeInstructionsContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void updateRecipe(MyRecipe myRecipe) {
        interactor.updateRecipe(myRecipe);
    }
}
