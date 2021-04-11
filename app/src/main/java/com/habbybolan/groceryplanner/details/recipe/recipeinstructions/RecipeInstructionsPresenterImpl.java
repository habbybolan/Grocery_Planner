package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

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
    public void updateRecipe(Recipe recipe) {
        recipeInstructionsInteractor.updateRecipe(recipe);
    }


    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeInstructionsInteractor.deleteRecipe(recipe);
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
