package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewPresenterImpl implements RecipeOverviewPresenter {

    private RecipeOverviewInteractor recipeOverviewInteractor;
    private RecipeOverviewView view;
    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private ObservableArrayList<RecipeCategory> loadedRecipeCategories = new ObservableArrayList<>();

    public RecipeOverviewPresenterImpl(RecipeOverviewInteractor recipeOverviewInteractor) {
        this.recipeOverviewInteractor = recipeOverviewInteractor;
        setRecipeCategoryCallback();
    }

    // set up callback for loading recipeCategories
    private void setRecipeCategoryCallback() {
        loadedRecipeCategories.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<RecipeCategory>>() {
            @Override
            public void onChanged(ObservableList<RecipeCategory> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {
                // set the loaded recipe categories as loaded in
                loadingRecipeCategories = false;
            }
            @Override
            public void onItemRangeMoved(ObservableList<RecipeCategory> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
        });
    }

    @Override
    public void setView(RecipeOverviewView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        // todo:
    }

    @Override
    public void loadAllRecipeCategories() {
        // load the recipe categories only if it is not already loading
        if (isCategoriesReady()) {
            view.loadingStarted();
            loadingRecipeCategories = true;
            try {
                recipeOverviewInteractor.loadAllRecipeCategories(loadedRecipeCategories);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                view.loadingFailed("failed retrieving data");
                loadingRecipeCategories = false;
            }
        } else {
            view.loadingFailed("recipe categories are being loaded");
        }
    }

    /**
     * Signals if the categories are being loaded in.
     * @return  True if the recipe categories are loaded in
     */
    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    @Override
    public ArrayList<RecipeCategory> getAllCategories() {
        return loadedRecipeCategories;
    }

    @Override
    public void displayRecipeCategories() {
        // display recipe categories if all loaded in
        if (isCategoriesReady()) {
            String[] recipeCategoryNames = recipeOverviewInteractor.getNamedOfRecipeCategories(loadedRecipeCategories);
            view.createCategoriesAlertDialogue(recipeCategoryNames);
        } else {
            // otherwise, recipe categories not loaded in yet
            view.loadingFailed("recipe categories are being loaded");
        }
    }
}
