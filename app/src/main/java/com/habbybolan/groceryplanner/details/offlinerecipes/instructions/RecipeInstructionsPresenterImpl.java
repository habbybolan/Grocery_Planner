package com.habbybolan.groceryplanner.details.offlinerecipes.instructions;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeInstructionsPresenterImpl
        <U extends RecipeInstructionsContract.Interactor<T2>, T extends RecipeInstructionsContract.RecipeInstructionsView,
                T2 extends OfflineRecipe>
        implements RecipeInstructionsContract.Presenter<U, T, T2> {

    protected U interactor;
    protected T view;
    protected T2 recipe;

    @Inject
    public RecipeInstructionsPresenterImpl(U interactor) {
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

    private boolean isViewAttached() {
        return view != null;
    }
}
