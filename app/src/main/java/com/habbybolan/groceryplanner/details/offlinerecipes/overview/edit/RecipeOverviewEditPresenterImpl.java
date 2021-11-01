package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewEditPresenterImpl extends RecipeOverviewPresenterImpl<RecipeOverviewContract.InteractorEdit, RecipeOverviewContract.OverviewEditView> implements RecipeOverviewContract.PresenterEdit {

    private boolean loadingGroceriesRecipeNotIn = false;
    // all grocery lists the recipe is not added to
    private List<Grocery> loadedGroceriesNotHoldingRecipe = new ArrayList<>();

    private boolean loadingIngredientsWithCheck = false;
    private List<IngredientWithGroceryCheck> loadedIngredientsWithCheck = new ArrayList<>();

    // user selected grocery list that also holds this recipe
    private Grocery selectedGrocery;

    private DbCallback<RecipeCategory> recipeCategoryDbCallback = new DbCallback<RecipeCategory>() {
        @Override
        public void onResponse(List<RecipeCategory> response) {
            // set the loaded recipe categories as loaded in
            loadedRecipeCategories.clear();
            loadedRecipeCategories.addAll(response);
            loadingRecipeCategories = false;
        }
    };

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

    public RecipeOverviewEditPresenterImpl(RecipeOverviewContract.InteractorEdit interactor) {
        super(interactor);
    }

    private void displayIngredientsWithCheck() {
        String[] ingredientNames = interactor.getArrayOfIngredientNames(loadedIngredientsWithCheck);
        List<IngredientWithGroceryCheck> ingredients = interactor.checkIfAllUnselected(loadedIngredientsWithCheck);
        view.displayRecipeIngredients(ingredients, ingredientNames, selectedGrocery);
    }

    @Override
    public void loadAllRecipeCategories() {
        // load the recipe categories only if it is not already loading
        if (isCategoriesReady()) {
            view.loadingStarted();
            loadingRecipeCategories = true;
            try {
                interactor.loadAllRecipeCategories(recipeCategoryDbCallback);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                view.loadingFailed("failed retrieving data");
                loadingRecipeCategories = false;
            }
        } else {
            view.loadingFailed("recipe categories are being loaded");
        }
    }

    /**
     * Signals if the categories are being loaded in.
     * @return  True if the recipe categories are loaded in
     */
    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    @Override
    public List<RecipeCategory> getAllCategories() {
        return loadedRecipeCategories;
    }

    @Override
    public void displayRecipeCategories() {
        // display recipe categories if all loaded in
        if (isCategoriesReady()) {
            String[] recipeCategoryNames = interactor.getNamedOfRecipeCategories(loadedRecipeCategories);
            view.createCategoriesAlertDialogue(recipeCategoryNames);
        } else {
            // otherwise, recipe categories not loaded in yet
            view.loadingFailed("recipe categories are being loaded");
        }
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

    @Override
    public void updateRecipeIngredientsInGrocery(OfflineRecipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        interactor.updateRecipeIngredientsInGrocery(recipe, grocery, amount, ingredients);
        fetchGroceriesHoldingRecipe(recipe);
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
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        return interactor.getArrayOfGroceryNames(groceries);
    }

    @Override
    public void deleteRecipeFromGrocery(OfflineRecipe recipe, Grocery grocery) {
        interactor.deleteRecipeFromGrocery(recipe , grocery);
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe recipe, RecipeTag recipeTag) {
        interactor.deleteRecipeTag(recipe, recipeTag);
    }

    @Override
    public void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, OfflineRecipe recipe) {
        // todo: make this a callable structure
        if (interactor.addTag(recipeTags, recipe, title)) {
            view.updateTagDisplay();
        } else {
            view.loadingFailed("Invalid Recipe tag name");
        }
    }

    @Override
    public void updateRecipe(OfflineRecipe recipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        interactor.updateRecipe(recipe, name, servingSize, cookTime, prepTime, description, category);
    }
}