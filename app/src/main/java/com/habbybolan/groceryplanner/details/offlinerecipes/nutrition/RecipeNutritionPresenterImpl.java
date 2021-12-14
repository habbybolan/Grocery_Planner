package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class RecipeNutritionPresenterImpl
        <U extends RecipeNutritionContract.Interactor<T2>, T extends RecipeNutritionContract.NutritionView,
                T2 extends OfflineRecipe>
        implements RecipeNutritionContract.Presenter<U, T, T2> {

    protected U interactor;
    protected T view;
    protected T2 recipe;

    public RecipeNutritionPresenterImpl(U interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setupPresenter(T view, long recipeId) {
        this.view = view;
        interactor.fetchFullRecipe(recipeId, new DbSingleCallbackWithFail<T2>() {
            @Override
            public void onFail(String message) {
                // TODO:
            }

            @Override
            public void onResponse(T2 response) {
                recipe = response;
                view.setupRecipeViews();
            }
        });
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public T2 getRecipe() {
        return recipe;
    }

    @Override
    public void loadUpdatedRecipe() {
        interactor.fetchFullRecipe(recipe.getId(), new DbSingleCallbackWithFail<T2>() {
            @Override
            public void onFail(String message) {
                // TODO:
            }

            @Override
            public void onResponse(T2 response) {
                recipe = response;
                view.displayUpdatedRecipe();
            }
        });
    }
}
