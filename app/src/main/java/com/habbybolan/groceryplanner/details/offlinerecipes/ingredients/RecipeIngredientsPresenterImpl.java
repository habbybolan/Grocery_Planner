package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeIngredientsPresenterImpl
        <U extends RecipeIngredientsContract.Interactor<T2>, T extends RecipeIngredientsContract.IngredientsView,
                T2 extends OfflineRecipe>
        implements RecipeIngredientsContract.Presenter<U, T, T2> {

    protected U interactor;
    protected T view;
    protected T2 recipe;

    private List<Ingredient> loadedIngredients = new ArrayList<>();

    private DbCallback<Ingredient> ingredientDbCallback = new DbCallback<Ingredient>() {
        @Override
        public void onResponse(List<Ingredient> response) {
            // set the loaded ingredients as loaded in
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            displayIngredientList();
        }
    };

    @Inject
    public RecipeIngredientsPresenterImpl(U interactor) {
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

    private void displayIngredientList() {
        if (isViewAttached())
            view.showList(loadedIngredients);
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void createIngredientList() {
        try {
            interactor.fetchIngredients(recipe, view.getSortType(), ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchIngredients(String ingredientSearch)  {
        try {
            interactor.searchIngredients(recipe, ingredientSearch, ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
                view.showList(recipe.getIngredients());
            }
        });
    }
}
