package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit;

import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeInstructionsEditPresenterImpl
        extends RecipeInstructionsPresenterImpl<RecipeInstructionsContract.InteractorEdit,
            RecipeInstructionsContract.RecipeInstructionsView, MyRecipe>
        implements RecipeInstructionsContract.PresenterEdit {

    public RecipeInstructionsEditPresenterImpl(RecipeInstructionsContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void updateRecipe() {
        interactor.updateRecipe(recipe);
    }
}
