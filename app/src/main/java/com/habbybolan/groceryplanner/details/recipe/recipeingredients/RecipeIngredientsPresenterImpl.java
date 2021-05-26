package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

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
    public void editRecipeName(OfflineRecipe offlineRecipe, String name) {
        // todo:
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        recipeIngredientsInteractor.deleteRecipe(offlineRecipe);
    }

    @Override
    public void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient) {
        recipeIngredientsInteractor.deleteIngredient(offlineRecipe, ingredient);
        createIngredientList(offlineRecipe);
    }

    @Override
    public void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients) {
        recipeIngredientsInteractor.deleteIngredients(offlineRecipe, ingredients);
        createIngredientList(offlineRecipe);
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
    public void createIngredientList(OfflineRecipe offlineRecipe) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            recipeIngredientsInteractor.fetchIngredients(offlineRecipe, view.getSortType(), ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredients");
            loadingIngredients = false;
        }
    }

    @Override
    public void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch)  {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            recipeIngredientsInteractor.searchIngredients(offlineRecipe, ingredientSearch, ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
