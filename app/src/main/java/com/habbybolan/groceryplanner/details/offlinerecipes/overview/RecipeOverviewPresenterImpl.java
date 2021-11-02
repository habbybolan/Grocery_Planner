package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewPresenterImpl<U extends RecipeOverviewContract.Interactor, T extends RecipeOverviewContract.OverviewView> implements RecipeOverviewContract.Presenter<T> {

    public RecipeOverviewPresenterImpl(U interactor) {
        this.interactor = interactor;
        setRecipeCategoryCallback();
    }

    protected U interactor;
    protected T view;

    // the current recipe category this recipe is in
    protected ObservableField<RecipeCategory> currRecipeCategory = new ObservableField<>();

    protected boolean loadingRecipeCategories = false;
    // all loaded recipe categories stored offline
    protected List<RecipeCategory> loadedRecipeCategories = new ArrayList<>();

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public RecipeCategory getRecipeCategory(int position) {
        if (position == loadedRecipeCategories.size())
            return null;
        return loadedRecipeCategories.get(position);
    }

    @Override
    public void fetchRecipeCategory(Long categoryId) {
        if (categoryId != null) {
            try {
                interactor.fetchRecipeCategory(currRecipeCategory, categoryId);
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

    /**
     * Displays the current recipe category.
     */
    private void displayCurrRecipeCategory() {
        view.displayRecipeCategory(currRecipeCategory.get());
    }

    private void displayRecipeTags() {
        view.displayRecipeTags();
    }
}
