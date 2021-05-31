package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeIngredientsPresenterImpl implements RecipeIngredientsContract.Presenter {

    private RecipeIngredientsContract.Interactor interactor;
    private RecipeIngredientsContract.IngredientsView view;

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

    @Inject
    public RecipeIngredientsPresenterImpl(RecipeIngredientsContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RecipeIngredientsContract.IngredientsView view) {
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
    public void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient) {
        interactor.deleteIngredient(offlineRecipe, ingredient);
        createIngredientList(offlineRecipe);
    }

    @Override
    public void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients) {
        interactor.deleteIngredients(offlineRecipe, ingredients);
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
            interactor.fetchIngredients(offlineRecipe, view.getSortType(), ingredientDbCallback);
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
            interactor.searchIngredients(offlineRecipe, ingredientSearch, ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
            view.loadingFailed("Failed to retrieve data");
        }
    }
}
