package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeCategoryPresenterImpl implements RecipeCategoryPresenter {

    private RecipeCategoryInteractor recipeCategoryInteractor;
    private RecipeCategoryView view;

    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private List<RecipeCategory> loadedRecipeCategories = new ArrayList<>();

    private DbCallback<RecipeCategory> recipeCategoryDbCallback = new DbCallback<RecipeCategory>() {
        @Override
        public void onResponse(List<RecipeCategory> response) {
            loadedRecipeCategories.clear();
            loadedRecipeCategories.addAll(response);
            loadingRecipeCategories = false;
            displayRecipeCategories();
        }
    };

    public RecipeCategoryPresenterImpl(RecipeCategoryInteractor recipeCategoryInteractor) {
        this.recipeCategoryInteractor = recipeCategoryInteractor;
    }


    @Override
    public void destroy() {
        view = null;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void deleteRecipeCategory(RecipeCategory recipeCategory) {
        recipeCategoryInteractor.deleteRecipeCategory(recipeCategory);
    }

    @Override
    public void deleteRecipeCategories(List<RecipeCategory> recipeCategories) {
        try {
            recipeCategoryInteractor.deleteRecipeCategories(recipeCategories);
        } finally {
            fetchRecipeCategories();
        }
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        recipeCategoryInteractor.addRecipeCategory(recipeCategory);
        fetchRecipeCategories();
    }

    @Override
    public void setView(RecipeCategoryView view) {
        this.view = view;
    }

    @Override
    public void fetchRecipeCategories() {
        try {
            loadingRecipeCategories = true;
            recipeCategoryInteractor.fetchRecipeCategories(recipeCategoryDbCallback, view.getSortType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve Recipe Categories");
            loadingRecipeCategories = false;
        }
    }

    @Override
    public void searchRecipeCategories(String categorySearch) {
        try {
            loadingRecipeCategories = true;
            view.loadingStarted();
            recipeCategoryInteractor.searchRecipeCategories(categorySearch, recipeCategoryDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingRecipeCategories = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }

    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    private void displayRecipeCategories() {
        if (isViewAttached() && isCategoriesReady())
            view.showList(loadedRecipeCategories);
    }
}
