package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewPresenterImpl implements RecipeOverviewContract.Presenter {

    private RecipeOverviewContract.Interactor interactor;
    private RecipeOverviewContract.OverviewView view;
    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private List<RecipeCategory> loadedRecipeCategories = new ArrayList<>();

    private ObservableField<RecipeCategory> currRecipeCategory = new ObservableField<>();

    private boolean loadingGroceriesRecipeIn = false;
    private List<GroceryRecipe> loadedGroceriesHoldingRecipe = new ArrayList<>();

    private boolean loadingGroceriesRecipeNotIn = false;
    private List<Grocery> loadedGroceriesNotHoldingRecipe = new ArrayList<>();

    private boolean loadingIngredientsWithCheck = false;
    private List<IngredientWithGroceryCheck> loadedIngredientsWithCheck = new ArrayList<>();

    private boolean loadingRecipeTags = false;
    private List<RecipeTag> loadedRecipeTags = new ArrayList<>();

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

    private DbCallback<RecipeTag> recipeTagDbCallback = new DbCallback<RecipeTag>() {
        @Override
        public void onResponse(List<RecipeTag> response) {
            loadedRecipeTags.clear();
            loadedRecipeTags.addAll(response);
            loadingRecipeTags = false;
            displayRecipeTags();
        }
    };

    public RecipeOverviewPresenterImpl(RecipeOverviewContract.Interactor interactor) {
        this.interactor = interactor;
        setRecipeCategoryCallback();
    }

    private void displayRecipeTags() {
        view.displayRecipeTags(loadedRecipeTags);
    }

    private void displayIngredientsWithCheck() {
        String[] ingredientNames = interactor.getArrayOfIngredientNames(loadedIngredientsWithCheck);
        List<IngredientWithGroceryCheck> ingredients = interactor.checkIfAllUnselected(loadedIngredientsWithCheck);
        view.displayRecipeIngredients(ingredients, ingredientNames, selectedGrocery);
    }

    // set up callback for loading recipeCategories
    private void setRecipeCategoryCallback() {

        currRecipeCategory.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                // once the current RecipeCategory is loaded, display it
                displayCurrRecipeCategory();
            }
        });
    }

    @Override
    public void setView(RecipeOverviewContract.OverviewView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
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

    /**
     * Displays the current recipe category.
     */
    private void displayCurrRecipeCategory() {
        view.displayRecipeCategory(currRecipeCategory.get());
    }

    private void displayGroceriesHoldingRecipe() {
        view.displayGroceriesHoldingRecipe(loadedGroceriesHoldingRecipe);
    }


    @Override
    public RecipeCategory getRecipeCategory(int position) {
        if (position == loadedRecipeCategories.size())
            return null;
        return loadedRecipeCategories.get(position);
    }

    @Override
    public void fetchRecipeCategory(Long categoryId) {
        if (categoryId != null) {
            try {
                interactor.fetchRecipeCategory(currRecipeCategory, categoryId);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getCurrCategoryName() {
        if (currRecipeCategory != null)
            return currRecipeCategory.get().getName();
        else return "";
    }

    @Override
    public void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe) {
        try {
            loadingGroceriesRecipeIn = true;
            view.loadingStarted();
            interactor.fetchGroceriesHoldingRecipe(offlineRecipe, groceryRecipeDbCallback);
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            loadingGroceriesRecipeIn = false;
        }
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe) {
        try {
            loadingGroceriesRecipeNotIn = true;
            view.loadingStarted();
            interactor.fetchGroceriesNotHoldingRecipe(offlineRecipe, groceryRecipeNotDbCallback);
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
    public void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        interactor.updateRecipeIngredientsInGrocery(offlineRecipe, grocery, amount, ingredients);
        fetchGroceriesHoldingRecipe(offlineRecipe);
    }

    @Override
    public void fetchRecipeIngredients(OfflineRecipe offlineRecipe, Grocery grocery, boolean isNotInGrocery) {
        try {
            loadingIngredientsWithCheck = true;
            view.loadingStarted();
            selectedGrocery = grocery;
            interactor.fetchRecipeIngredients(offlineRecipe, grocery, isNotInGrocery, ingredientWithGroceryCheckDbCallback);
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
    public void deleteRecipeFromGrocery(OfflineRecipe offlineRecipe, Grocery grocery) {
        interactor.deleteRecipeFromGrocery(offlineRecipe, grocery);
    }

    @Override
    public void createRecipeTagList(OfflineRecipe offlineRecipe) {
        try {
            loadingRecipeTags = true;
            interactor.fetchTags(offlineRecipe, recipeTagDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            loadingRecipeTags = false;
            view.loadingFailed("Failed to retrieve tags");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe offlineRecipe, RecipeTag recipeTag) {
        interactor.deleteRecipeTag(offlineRecipe, recipeTag);
    }

    @Override
    public void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, OfflineRecipe offlineRecipe) {
        if (interactor.addTag(recipeTags, offlineRecipe, title)) {
            view.updateTagDisplay();
        } else {
            view.loadingFailed("Invalid Recipe tag name");
        }
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        interactor.updateRecipe(offlineRecipe, name, servingSize, cookTime, prepTime, description, category);
    }
}
