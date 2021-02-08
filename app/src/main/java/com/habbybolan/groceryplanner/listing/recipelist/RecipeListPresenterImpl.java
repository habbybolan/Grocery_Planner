package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private ListViewInterface view;
    private RecipeListInteractor recipeListInteractor;
    private List<Recipe> loadedRecipes = new ArrayList<>();

    public RecipeListPresenterImpl(RecipeListInteractor recipeListInteractor) {
        this.recipeListInteractor = recipeListInteractor;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipeListInteractor.addRecipe(recipe);
        try {
            loadedRecipes = recipeListInteractor.fetchRecipes();
            if (isViewAttached()) {
                view.showList(loadedRecipes);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to retrieve data");
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    @Override
    public void createRecipeList() {
        try {
            view.loadingStarted();
            loadedRecipes = recipeListInteractor.fetchRecipes();
            if (isViewAttached())
                view.showList(loadedRecipes);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
