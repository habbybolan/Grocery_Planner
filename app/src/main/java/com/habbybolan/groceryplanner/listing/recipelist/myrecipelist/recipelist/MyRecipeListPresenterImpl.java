package com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListState;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRecipeListPresenterImpl implements MyRecipeListContract.Presenter {

    private MyRecipeListContract.View view;
    private RecipeListState state;
    private MyRecipeListContract.Interactor interactor;

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

    // callback for adding a new recipe
    private DbSingleCallback<OfflineRecipe> recipeAddDbCallback = new DbSingleCallback<OfflineRecipe>() {
        @Override
        public void onResponse(OfflineRecipe response) {
            view.onRecipeAdded(response);
        }
    };

    public MyRecipeListPresenterImpl(MyRecipeListContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void destroy() {
        view = null;
        state = null;
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        interactor.deleteRecipe(offlineRecipe);
        createRecipeList();
    }

    @Override
    public void deleteRecipes(List<OfflineRecipe> offlineRecipes) {
        interactor.deleteRecipes(offlineRecipes);
        createRecipeList();
    }

    @Override
    public void addRecipe(OfflineRecipe offlineRecipe) {
        try {
            loadingRecipes = true;
            interactor.addRecipe(offlineRecipe, recipeAddDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to add recipe");
            loadingRecipes = false;
        }
    }

    @Override
    public void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipes, RecipeCategory category) {
        interactor.addRecipesToCategory(offlineRecipes, category);
        createRecipeList();
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes) {
        interactor.removeRecipesFromCategory(offlineRecipes);
        createRecipeList();
    }

    private boolean isViewAttached() {
        if (view == null) throw new IllegalStateException("View is not attached");
        return true;
    }

    private boolean isStateAttached() {
        if (state == null) throw new IllegalStateException("State object is not attached");
        return true;
    }

    @Override
    public void setView(MyRecipeListContract.View view) {
        this.view = view;
    }

    @Override
    public void setState(RecipeListState state) {
        this.state = state;
    }

    @Override
    public void createRecipeList() {
        try {
            view.loadingStarted();
            loadingRecipes = true;
            if (isViewAttached() && isStateAttached()) interactor.fetchRecipes(state.getRecipeCategory(), view.getSortType(), recipeDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingRecipes = false;
        }
    }

    private boolean isRecipesReady() {
        return !loadingRecipes;
    }

    /**
     * Check if view exists, and display loaded recipes.
     */
    private void displayRecipes() {
        if (isViewAttached() && isRecipesReady())
            view.showList(loadedOfflineRecipes);
    }

    @Override
    public void fetchCategories() {
        try {
            loadingRecipeCategories = true;
            view.loadingStarted();
            interactor.fetchCategories(recipeCategoryDbCallback);
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
            if (isViewAttached() && isStateAttached()) interactor.searchRecipes(state.getRecipeCategory(), recipeSearch, recipeDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingRecipes = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
