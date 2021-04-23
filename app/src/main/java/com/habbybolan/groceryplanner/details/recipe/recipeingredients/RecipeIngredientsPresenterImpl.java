package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeIngredientsPresenterImpl implements RecipeIngredientsPresenter {

    private RecipeIngredientsInteractor recipeIngredientsInteractor;
    private ListViewInterface view;

    // true if the ingredients are being loaded
    private boolean loadingIngredients = false;
    private List<Ingredient> loadedIngredients = new ArrayList<>();

    private DbCallback<Ingredient> ingredientDbCallback = new DbCallback<Ingredient>() {
        @Override
        public void onResponse(List<Ingredient> response) {
            // set the loaded ingredients as loaded in
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            loadingIngredients = false;
            displayIngredientList();
        }
    };

    public RecipeIngredientsPresenterImpl(RecipeIngredientsInteractor recipeIngredientsInteractor) {
        this.recipeIngredientsInteractor = recipeIngredientsInteractor;
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void editRecipeName(Recipe recipe, String name) {
        // todo:
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeIngredientsInteractor.deleteRecipe(recipe);
    }

    @Override
    public void deleteIngredient(Recipe recipe, Ingredient ingredient) {
        recipeIngredientsInteractor.deleteIngredient(recipe, ingredient);
        createIngredientList(recipe);
    }

    @Override
    public void deleteIngredients(Recipe recipe, List<Ingredient> ingredients) {
        recipeIngredientsInteractor.deleteIngredients(recipe, ingredients);
        createIngredientList(recipe);
    }

    /**
     * Signals if the ingredients are being loaded in.
     * @return  True if the ingredients are loaded in
     */
    private boolean isIngredientsReady() {
        return !loadingIngredients;
    }

    private void displayIngredientList() {
        if (isViewAttached() && isIngredientsReady())
            view.showList(loadedIngredients);
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void createIngredientList(Recipe recipe) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            recipeIngredientsInteractor.fetchIngredients(recipe, view.getSortType(), ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredients");
            loadingIngredients = false;
        }
    }

    @Override
    public void searchIngredients(Recipe recipe, String ingredientSearch)  {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            recipeIngredientsInteractor.searchIngredients(recipe, ingredientSearch, ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
