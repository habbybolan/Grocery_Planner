package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.models.Recipe;

public class RecipeNutritionPresenterImpl implements RecipeNutritionPresenter {

    RecipeNutritionInteractor interactor;
    RecipeNutritionView view;

    public RecipeNutritionPresenterImpl(RecipeNutritionInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        interactor.updateRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        interactor.deleteRecipe(recipe);
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
