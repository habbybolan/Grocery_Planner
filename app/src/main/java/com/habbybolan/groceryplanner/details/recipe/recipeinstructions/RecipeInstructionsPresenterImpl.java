package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class RecipeInstructionsPresenterImpl implements RecipeInstructionsPresenter {

    private RecipeInstructionsInteractor recipeInstructionsInteractor;
    private RecipeInstructionsView view;

    public RecipeInstructionsPresenterImpl(RecipeInstructionsInteractor recipeInstructionsInteractor) {
        this.recipeInstructionsInteractor = recipeInstructionsInteractor;
    }

    @Override
    public void setView(RecipeInstructionsView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        recipeInstructionsInteractor.updateRecipe(offlineRecipe);
    }


    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        recipeInstructionsInteractor.deleteRecipe(offlineRecipe);
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
