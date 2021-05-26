package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class RecipeNutritionPresenterImpl implements RecipeNutritionPresenter {

    RecipeNutritionInteractor interactor;
    RecipeNutritionView view;

    public RecipeNutritionPresenterImpl(RecipeNutritionInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        interactor.updateRecipe(offlineRecipe);
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        interactor.deleteRecipe(offlineRecipe);
    }

    @Override
    public void setView(RecipeNutritionView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }
}
