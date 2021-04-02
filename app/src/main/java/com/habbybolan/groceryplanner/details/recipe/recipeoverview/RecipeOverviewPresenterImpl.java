package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewPresenterImpl implements RecipeOverviewPresenter {

    private RecipeOverviewInteractor recipeOverviewInteractor;
    private RecipeOverviewView view;
    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private ObservableArrayList<RecipeCategory> loadedRecipeCategories = new ObservableArrayList<>();
    private ObservableField<RecipeCategory> currRecipeCategory = new ObservableField<>();

    private boolean loadingGroceriesRecipeIn = false;
    private ObservableArrayList<GroceryRecipe> loadedGroceriesHoldingRecipe = new ObservableArrayList<>();

    private boolean loadingGroceriesRecipeNotIn = false;
    private ObservableArrayList<Grocery> loadedGroceriesNotHoldingRecipe = new ObservableArrayList<>();

    private boolean loadingIngredientsWithCheck = false;
    private ObservableArrayList<IngredientWithGroceryCheck> loadedIngredientsWithCheck = new ObservableArrayList<>();

    private Grocery selectedGrocery;

    public RecipeOverviewPresenterImpl(RecipeOverviewInteractor recipeOverviewInteractor) {
        this.recipeOverviewInteractor = recipeOverviewInteractor;
        setRecipeCategoryCallback();
        setGroceriesHoldingRecipeCallback();
        setGroceriesNotHoldingRecipeCallback();
        setIngredientsWithCheckCallback();
    }

    private void setIngredientsWithCheckCallback() {
        loadedIngredientsWithCheck.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<IngredientWithGroceryCheck>>() {
            @Override
            public void onChanged(ObservableList<IngredientWithGroceryCheck> sender) {}
            @Override
            public void onItemRangeChanged(ObservableList<IngredientWithGroceryCheck> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<IngredientWithGroceryCheck> sender, int positionStart, int itemCount) {
                loadingIngredientsWithCheck = false;
                displayIngredientsWithCheck();
            }
            @Override
            public void onItemRangeMoved(ObservableList<IngredientWithGroceryCheck> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<IngredientWithGroceryCheck> sender, int positionStart, int itemCount) {}
        });
    }

    private void displayIngredientsWithCheck() {
        String[] ingredients = recipeOverviewInteractor.getArrayOfIngredientNames(loadedIngredientsWithCheck);
        view.displayRecipeIngredients(loadedIngredientsWithCheck, ingredients, selectedGrocery);
    }

    private void setGroceriesNotHoldingRecipeCallback() {
        loadedGroceriesNotHoldingRecipe.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Grocery>>() {
            @Override
            public void onChanged(ObservableList<Grocery> sender) { }
            @Override
            public void onItemRangeChanged(ObservableList<Grocery> sender, int positionStart, int itemCount) { }
            @Override
            public void onItemRangeInserted(ObservableList<Grocery> sender, int positionStart, int itemCount) {
                loadingGroceriesRecipeNotIn = false;
            }
            @Override
            public void onItemRangeMoved(ObservableList<Grocery> sender, int fromPosition, int toPosition, int itemCount) { }
            @Override
            public void onItemRangeRemoved(ObservableList<Grocery> sender, int positionStart, int itemCount) { }
        });
    }

    private void setGroceriesHoldingRecipeCallback() {
        loadedGroceriesHoldingRecipe.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<GroceryRecipe>>() {
            @Override
            public void onChanged(ObservableList<GroceryRecipe> sender) {}
            @Override
            public void onItemRangeChanged(ObservableList<GroceryRecipe> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<GroceryRecipe> sender, int positionStart, int itemCount) {
                loadingGroceriesRecipeIn = false;
                displayGroceriesHoldingRecipe();
            }
            @Override
            public void onItemRangeMoved(ObservableList<GroceryRecipe> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<GroceryRecipe> sender, int positionStart, int itemCount) {
                loadingGroceriesRecipeIn = false;
                displayGroceriesHoldingRecipe();
            }
        });
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
                displayIngredientsWithCheck();
            }
            @Override
            public void onItemRangeMoved(ObservableList<RecipeCategory> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
        });

        currRecipeCategory.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                // once the current RecipeCategory is loaded, display it
                displayCurrRecipeCategory();
            }
        });
    }

    @Override
    public void setView(RecipeOverviewView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeOverviewInteractor.deleteRecipe(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeOverviewInteractor.updateRecipe(recipe);
    }

    @Override
    public void loadAllRecipeCategories() {
        // load the recipe categories only if it is not already loading
        if (isCategoriesReady()) {
            view.loadingStarted();
            loadingRecipeCategories = true;
            try {
                recipeOverviewInteractor.loadAllRecipeCategories(loadedRecipeCategories);
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
    public ArrayList<RecipeCategory> getAllCategories() {
        return loadedRecipeCategories;
    }

    @Override
    public void displayRecipeCategories() {
        // display recipe categories if all loaded in
        if (isCategoriesReady()) {
            String[] recipeCategoryNames = recipeOverviewInteractor.getNamedOfRecipeCategories(loadedRecipeCategories);
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
                recipeOverviewInteractor.fetchRecipeCategory(currRecipeCategory, categoryId);
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
    public void fetchGroceriesHoldingRecipe(Recipe recipe) {
        try {
            loadingGroceriesRecipeIn = true;
            view.loadingStarted();
            recipeOverviewInteractor.fetchGroceriesHoldingRecipe(recipe, loadedGroceriesHoldingRecipe);
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            loadingGroceriesRecipeIn = false;
        }
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(Recipe recipe) {
        try {
            loadingGroceriesRecipeNotIn = true;
            view.loadingStarted();
            recipeOverviewInteractor.fetchGroceriesNotHoldingRecipe(recipe, loadedGroceriesNotHoldingRecipe);
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
            loadingGroceriesRecipeNotIn = false;
        }
    }

    @Override
    public void displayGroceriesNotHoldingRecipe() {
        if (!loadingGroceriesRecipeNotIn) {
            view.displayGroceriesNotHoldingRecipe(loadedGroceriesNotHoldingRecipe);
        } else {
            view.loadingFailed("Loading not finished");
        }
    }

    @Override
    public void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        recipeOverviewInteractor.updateRecipeIngredientsInGrocery(recipe, grocery, amount, ingredients);
        fetchGroceriesHoldingRecipe(recipe);
    }

    @Override
    public void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery) {
        try {
            loadingIngredientsWithCheck = true;
            view.loadingStarted();
            selectedGrocery = grocery;
            recipeOverviewInteractor.fetchRecipeIngredients(recipe, grocery, isNotInGrocery, loadedIngredientsWithCheck);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            loadingIngredientsWithCheck = false;
        }
    }

    @Override
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        return recipeOverviewInteractor.getArrayOfGroceryNames(groceries);
    }

    @Override
    public void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery) {
        recipeOverviewInteractor.deleteRecipeFromGrocery(recipe, grocery);
    }
}
