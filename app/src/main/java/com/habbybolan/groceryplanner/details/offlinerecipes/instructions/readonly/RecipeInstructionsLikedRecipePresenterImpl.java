package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

public class RecipeInstructionsLikedRecipePresenterImpl extends RecipeInstructionsPresenterImpl<RecipeInstructionsContract.InteractorLikedRecipe,
        RecipeInstructionsContract.RecipeInstructionsView, LikedRecipe> implements RecipeInstructionsContract.PresenterLikedRecipe {

    public RecipeInstructionsLikedRecipePresenterImpl(RecipeInstructionsContract.InteractorLikedRecipe interactor) {
        super(interactor);
    }
}
