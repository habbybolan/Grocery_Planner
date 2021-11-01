package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsPresenterImpl;

public class RecipeInstructionsReadOnlyPresenterImpl extends RecipeInstructionsPresenterImpl<RecipeInstructionsContract.Interactor, RecipeInstructionsContract.RecipeInstructionsView> implements RecipeInstructionsContract.PresenterReadOnly {

    public RecipeInstructionsReadOnlyPresenterImpl(RecipeInstructionsContract.Interactor interactor) {
        super(interactor);
    }
}
