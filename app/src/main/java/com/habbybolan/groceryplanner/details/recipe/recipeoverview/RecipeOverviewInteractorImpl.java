package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewInteractorImpl implements RecipeOverviewInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
    @Override
    public void updateRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe);
    }

    @Override
    public void loadAllRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategories(recipeCategoriesObserved);
    }

    @Override
    public String[] getNamedOfRecipeCategories(ArrayList<RecipeCategory> recipeCategories) {
        String[] categoryNames = new String[recipeCategories.size()+1];
        for (int i = 0; i < recipeCategories.size(); i++) {
            categoryNames[i] = recipeCategories.get(i).getName();
        }
        categoryNames[recipeCategories.size()] = "No Category";
        return categoryNames;
    }

    @Override
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }

    @Override
    public void fetchGroceriesHoldingRecipe(Recipe recipe, ObservableArrayList<GroceryRecipe> groceriesObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesHoldingRecipe(recipe, groceriesObserver);
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(Recipe recipe, ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesNotHoldingRecipe(recipe, groceriesObserver);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        databaseAccess.updateRecipeIngredientsInGrocery(recipe, grocery, amount, ingredients);
        // loop through the ingredients and find if all of them are not added to Grocery list
        // if none added, then delete the recipe from the GroceryRecipeBridge
        boolean hasIngredientSelected = false;
        for (IngredientWithGroceryCheck ingredient : ingredients) {
            if (ingredient.getIsInGrocery()) {
                hasIngredientSelected = true;
                break;
            }
        }
        // if no ingredient inside the grocery list, then remove the recipe grocery relationship
        if (!hasIngredientSelected)
            databaseAccess.deleteGroceryRecipeBridge(recipe, grocery);
    }

    @Override
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        String[] groceryNames = new String[groceries.size()];
        for (int i = 0; i < groceries.size(); i++) {
            groceryNames[i] = groceries.get(i).getName();
        }
        return groceryNames;
    }

    @Override
    public String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients) {
        String[] ingredientNames = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientNames[i] = ingredients.get(i).getName();
        }
        return ingredientNames;
    }

    @Override
    public void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery, ObservableArrayList<IngredientWithGroceryCheck> ingredientsObserver) throws ExecutionException, InterruptedException {
       if (isNotInGrocery) {
           databaseAccess.fetchRecipeIngredientsNotInGrocery(recipe, ingredientsObserver);
       } else {
           databaseAccess.fetchRecipeIngredientsInGrocery(grocery, recipe, ingredientsObserver);
       }
    }

    @Override
    public void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery) {
        databaseAccess.deleteRecipeFromGrocery(grocery, recipe);
    }

}
