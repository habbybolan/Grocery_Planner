package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.instructions;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

public interface OnlineRecipeInstructionsContract {

    interface instructionsListener {
        OnlineRecipe getRecipe();
    }
}
