package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewPresenterImpl
        <U extends RecipeOverviewContract.Interactor<T2>, T extends RecipeOverviewContract.OverviewView,
                T2 extends OfflineRecipe>
        implements RecipeOverviewContract.Presenter<U, T, T2> {

    public RecipeOverviewPresenterImpl(U interactor) {
        this.interactor = interactor;
        setRecipeCategoryCallback();
    }

    protected U interactor;
    protected T view;
    protected T2 recipe;

    // the current recipe category this recipe is in
    protected ObservableField<RecipeCategory> currRecipeCategory = new ObservableField<>();

    // all loaded recipe categories stored offline
    protected List<RecipeCategory> loadedRecipeCategories = new ArrayList<>();

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public RecipeCategory getRecipeCategory(int position) {
        if (position == loadedRecipeCategories.size())
            return null;
        return loadedRecipeCategories.get(position);
    }

    @Override
    public void fetchRecipeCategory() {
        if (recipe.getCategoryId() != null) {
            try {
                interactor.fetchRecipeCategory(currRecipeCategory, recipe.getCategoryId());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    // set up callback for loading recipeCategories
    private void setRecipeCategoryCallback() {
        currRecipeCategory.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                // once the current RecipeCategory is loaded, display it
                displayCurrRecipeCategory();
            }
        });
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

    @Override
    public T2 getRecipe() {
        return recipe;
    }

    /**
     * Displays the current recipe category.
     */
    private void displayCurrRecipeCategory() {
        view.displayRecipeCategory(currRecipeCategory.get());
    }
}
