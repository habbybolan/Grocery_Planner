package com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class AddRecipeToGroceryListPresenterImpl implements AddRecipeToGroceryListContract.Presenter {

    private AddRecipeToGroceryListContract.Interactor interactor;

    private AddRecipeToGroceryListContract.View view;

    private boolean loadingGroceriesRecipeNotIn = false;
    // all grocery lists the recipe is not added to
    private List<Grocery> loadedGroceriesNotHoldingRecipe = new ArrayList<>();

    private boolean loadingIngredientsWithCheck = false;
    private List<IngredientWithGroceryCheck> loadedIngredientsWithCheck = new ArrayList<>();

    // user selected grocery list that also holds this recipe
    private Grocery selectedGrocery;

    protected boolean loadingGroceriesRecipeIn = false;
    // all grocery lists where the recipe has been added to
    protected List<GroceryRecipe> loadedGroceriesHoldingRecipe = new ArrayList<>();

    // callback for retrieving groceries not holding current recipe
    private DbCallback<Grocery> groceryRecipeNotDbCallback = new DbCallback<Grocery>() {
        @Override
        public void onResponse(List<Grocery> response) {
            loadedGroceriesNotHoldingRecipe.clear();
            loadedGroceriesNotHoldingRecipe.addAll(response);
            loadingGroceriesRecipeNotIn = false;
        }
    };

    private DbCallback<IngredientWithGroceryCheck> ingredientWithGroceryCheckDbCallback = new DbCallback<IngredientWithGroceryCheck>() {
        @Override
        public void onResponse(List<IngredientWithGroceryCheck> response) {
            loadedIngredientsWithCheck.clear();
            loadedIngredientsWithCheck.addAll(response);
            loadingIngredientsWithCheck = false;
            displayIngredientsWithCheck();
        }
    };

    private void displayIngredientsWithCheck() {
        String[] ingredientNames = interactor.getArrayOfIngredientNames(loadedIngredientsWithCheck);
        List<IngredientWithGroceryCheck> ingredients = interactor.checkIfAllUnselected(loadedIngredientsWithCheck);
        view.displayRecipeIngredients(ingredients, ingredientNames, selectedGrocery);
    }


    // callback for retrieving groceries holding current recipe
    private DbCallback<GroceryRecipe> groceryRecipeDbCallback = new DbCallback<GroceryRecipe>() {
        @Override
        public void onResponse(List<GroceryRecipe> response) {
            loadedGroceriesHoldingRecipe.clear();
            loadedGroceriesHoldingRecipe.addAll(response);
            loadingGroceriesRecipeIn = false;
            displayGroceriesHoldingRecipe();
        }
    };

    @Inject
    public AddRecipeToGroceryListPresenterImpl(AddRecipeToGroceryListContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void setView(AddRecipeToGroceryListContract.View view) {
        this.view = view;
    }

    @Override
    public void fetchRecipeIngredients(OfflineRecipe recipe, Grocery grocery, boolean isNotInGrocery) {
        try {
            loadingIngredientsWithCheck = true;
            view.loadingStarted();
            selectedGrocery = grocery;
            interactor.fetchRecipeIngredients(recipe, grocery, isNotInGrocery, ingredientWithGroceryCheckDbCallback);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            loadingIngredientsWithCheck = false;
        }
    }

    @Override
    public void updateRecipeIngredientsInGrocery(OfflineRecipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        interactor.updateRecipeIngredientsInGrocery(recipe, grocery, amount, ingredients);
        fetchGroceriesHoldingRecipe(recipe);
    }

    @Override
    public void fetchGroceriesHoldingRecipe(OfflineRecipe recipe) {
        try {
            loadingGroceriesRecipeIn = true;
            view.loadingStarted();
            interactor.fetchGroceriesHoldingRecipe(recipe, groceryRecipeDbCallback);
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            loadingGroceriesRecipeIn = false;
        }
    }

    @Override
    public List<GroceryRecipe> getLoadedGroceriesHoldingRecipe() {
        return loadedGroceriesHoldingRecipe;
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(OfflineRecipe recipe) {
        try {
            loadingGroceriesRecipeNotIn = true;
            view.loadingStarted();
            interactor.fetchGroceriesNotHoldingRecipe(recipe, groceryRecipeNotDbCallback);
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            loadingGroceriesRecipeNotIn = false;
        }
    }

    @Override
    public void displayGroceriesNotHoldingRecipe() {
        if (loadedGroceriesNotHoldingRecipe.isEmpty()) {
            view.loadingFailed("Can't add recipe to any more groceries");
        } else if (!loadingGroceriesRecipeNotIn) {
            view.displayGroceriesNotHoldingRecipe(loadedGroceriesNotHoldingRecipe);
        } else {
            view.loadingFailed("Loading not finished");
        }
    }

    private void displayGroceriesHoldingRecipe() {
        view.displayGroceriesHoldingRecipe();
    }

    @Override
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        return interactor.getArrayOfGroceryNames(groceries);
    }

    @Override
    public void deleteRecipeFromGrocery(OfflineRecipe recipe, Grocery grocery) {
        interactor.deleteRecipeFromGrocery(recipe , grocery);
    }
}
