package com.habbybolan.groceryplanner.listing.recipelist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private RecipeListView view;
    private RecipeListInteractor recipeListInteractor;
    private RecipeCategory recipeCategory;

    // true of the recipes are being loaded in
    private boolean loadingRecipes = false;
    private ObservableArrayList<Recipe> loadedRecipes = new ObservableArrayList<>();
    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private ObservableArrayList<RecipeCategory> loadedRecipeCategories = new ObservableArrayList<>();

    public RecipeListPresenterImpl(RecipeListInteractor recipeListInteractor) {
        this.recipeListInteractor = recipeListInteractor;
        setRecipeCategoryCallback();
        setRecipeCallback();
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
                displayRecipeCategories();
            }
            @Override
            public void onItemRangeMoved(ObservableList<RecipeCategory> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
        });
    }

    private void setRecipeCallback() {
        loadedRecipes.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Recipe>>() {
            @Override
            public void onChanged(ObservableList<Recipe> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<Recipe> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<Recipe> sender, int positionStart, int itemCount) {
                // set the loaded recipe categories as loaded in
                loadingRecipes = false;
                displayRecipes();
            }
            @Override
            public void onItemRangeMoved(ObservableList<Recipe> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<Recipe> sender, int positionStart, int itemCount) {}
        });
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeListInteractor.deleteRecipe(recipe);
        try {
            view.loadingStarted();
            loadingRecipes = true;
            recipeListInteractor.fetchRecipes(recipeCategory, loadedRecipes);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve Recipe");
            loadingRecipes = false;
        }
    }

    @Override
    public void deleteRecipes(List<Recipe> recipes) {
        recipeListInteractor.deleteRecipes(recipes);
        try {
            view.loadingStarted();
            loadingRecipes = true;
            recipeListInteractor.fetchRecipes(recipeCategory, loadedRecipes);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve Recipes");
            loadingRecipes = false;
        }
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipeListInteractor.addRecipe(recipe);
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
            view.loadingStarted();
            recipeListInteractor.fetchRecipes(recipeCategory, loadedRecipes);
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
            recipeListInteractor.fetchCategories(loadedRecipeCategories);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingRecipeCategories = false;
        }
    }

    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    private void displayRecipeCategories() {
        if (isViewAttached() && isCategoriesReady())
            view.storeAllRecipeCategories(loadedRecipeCategories);
    }

    @Override
    public ArrayList<RecipeCategory> getLoadedRecipeCategories() {
        if (!isCategoriesReady())
            view.loadingFailed("recipe categories are being loaded");
        return new ArrayList<>(loadedRecipeCategories);
    }
}
