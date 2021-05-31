package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeInstructionsPresenterImpl implements RecipeInstructionsContract.Presenter {

    private RecipeInstructionsContract.Interactor interactor;
    private RecipeInstructionsContract.RecipeInstructionsView view;

    @Inject
    public RecipeInstructionsPresenterImpl(RecipeInstructionsContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RecipeInstructionsContract.RecipeInstructionsView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        interactor.updateRecipe(offlineRecipe);
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
