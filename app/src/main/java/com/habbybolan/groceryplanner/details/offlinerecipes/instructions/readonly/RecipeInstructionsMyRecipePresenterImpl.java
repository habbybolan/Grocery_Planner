package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeInstructionsMyRecipePresenterImpl extends RecipeInstructionsPresenterImpl<RecipeInstructionsContract.InteractorMyRecipe,
        RecipeInstructionsContract.RecipeInstructionsView, MyRecipe> implements RecipeInstructionsContract.PresenterMyRecipe {

    public RecipeInstructionsMyRecipePresenterImpl(RecipeInstructionsContract.InteractorMyRecipe interactor) {
        super(interactor);
    }
}
