package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private RecipeListView view;
    private RecipeListInteractor recipeListInteractor;

    // true of the recipes are being loaded in
    private boolean loadingRecipes = false;
    private List<OfflineRecipe> loadedOfflineRecipes = new ArrayList<>();
    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private List<RecipeCategory> loadedRecipeCategories = new ArrayList<>();

    private DbCallback<RecipeCategory> recipeCategoryDbCallback = new DbCallback<RecipeCategory>() {
        @Override
        public void onResponse(List<RecipeCategory> response) {
            loadedRecipeCategories.clear();
            loadedRecipeCategories.addAll(response);
            loadingRecipeCategories = false;
        }
    };

    private DbCallback<OfflineRecipe> recipeDbCallback = new DbCallback<OfflineRecipe>() {
        @Override
        public void onResponse(List<OfflineRecipe> response) {
            loadedOfflineRecipes.clear();
            loadedOfflineRecipes.addAll(response);
            loadingRecipes = false;
            displayRecipes();
        }
    };

    public RecipeListPresenterImpl(RecipeListInteractor recipeListInteractor) {
        this.recipeListInteractor = recipeListInteractor;
    }


    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        recipeListInteractor.deleteRecipe(offlineRecipe);
        createRecipeList();
    }

    @Override
    public void deleteRecipes(List<OfflineRecipe> offlineRecipes) {
        recipeListInteractor.deleteRecipes(offlineRecipes);
        createRecipeList();
    }

    @Override
    public void addRecipe(OfflineRecipe offlineRecipe, Timestamp dateCreated) {
        recipeListInteractor.addRecipe(offlineRecipe, dateCreated);
        createRecipeList();
    }

    @Override
    public void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipes, RecipeCategory category) {
        recipeListInteractor.addRecipesToCategory(offlineRecipes, category);
        createRecipeList();
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes) {
        recipeListInteractor.removeRecipesFromCategory(offlineRecipes);
        createRecipeList();
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setView(RecipeListView view) {
        this.view = view;
    }

    @Override
    public void createRecipeList() {
        try {
            view.loadingStarted();
            loadingRecipes = true;
            recipeListInteractor.fetchRecipes(view.getRecipeCategory(), view.getSortType(), recipeDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingRecipes = false;
        }
    }

    private boolean isRecipesReady() {
        return !loadingRecipes;
    }

    private void displayRecipes() {
        if (isViewAttached() && isRecipesReady())
            view.showList(loadedOfflineRecipes);
    }

    @Override
    public void fetchCategories() {
        try {
            loadingRecipeCategories = true;
            view.loadingStarted();
            recipeListInteractor.fetchCategories(recipeCategoryDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingRecipeCategories = false;
        }
    }

    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    @Override
    public ArrayList<RecipeCategory> getLoadedRecipeCategories() {
        if (!isCategoriesReady())
            view.loadingFailed("recipe categories are being loaded");
        return new ArrayList<>(loadedRecipeCategories);
    }

    @Override
    public void searchRecipes(String recipeSearch) {
        try {
            loadingRecipes = true;
            view.loadingStarted();
            recipeListInteractor.searchRecipes(view.getRecipeCategory(), recipeSearch, recipeDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingRecipes = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
