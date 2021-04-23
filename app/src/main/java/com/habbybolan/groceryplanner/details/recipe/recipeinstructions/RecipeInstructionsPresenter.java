package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractor;

public interface RecipeInstructionsPresenter extends RecipeDetailsInteractor {

    void setView(RecipeInstructionsView view);
    void destroy();
}
