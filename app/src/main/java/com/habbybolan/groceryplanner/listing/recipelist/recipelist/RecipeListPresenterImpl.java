package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private RecipeListView view;
    private RecipeListInteractor recipeListInteractor;
    private RecipeCategory recipeCategory;

    // true of the recipes are being loaded in
    private boolean loadingRecipes = false;
    private List<Recipe> loadedRecipes = new ArrayList<>();
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

    private DbCallback<Recipe> recipeDbCallback = new DbCallback<Recipe>() {
        @Override
        public void onResponse(List<Recipe> response) {
            loadedRecipes.clear();
            loadedRecipes.addAll(response);
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
    public void deleteRecipe(Recipe recipe) {
        recipeListInteractor.deleteRecipe(recipe);
        createRecipeList();
    }

    @Override
    public void deleteRecipes(List<Recipe> recipes) {
        recipeListInteractor.deleteRecipes(recipes);
        createRecipeList();
    }

    @Override
    public void addRecipe(Recipe recipe, Date dateCreated) {
        recipeListInteractor.addRecipe(recipe, dateCreated);
        createRecipeList();
    }

    @Override
    public void addRecipesToCategory(ArrayList<Recipe> recipes, RecipeCategory category) {
        recipeListInteractor.addRecipesToCategory(recipes, category);
        createRecipeList();
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<Recipe> recipes) {
        recipeListInteractor.removeRecipesFromCategory(recipes);
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
            recipeListInteractor.fetchRecipes(recipeCategory, view.getSortType(), recipeDbCallback);
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
            view.showList(loadedRecipes);
    }

    @Override
    public void attachCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
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
            recipeListInteractor.searchRecipes(recipeCategory, recipeSearch, recipeDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingRecipes = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
