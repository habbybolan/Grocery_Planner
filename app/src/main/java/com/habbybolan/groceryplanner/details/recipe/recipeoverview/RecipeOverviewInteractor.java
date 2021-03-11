package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewInteractor {

    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    void getCategory(Recipe recipe);


    void loadAllRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException;

    /**
     * Converts the list of RecipeCategory to an array of their names
     * @param recipeCategories  loaded recipe categories
     * @return                  Array of the loaded RecipeCategory names to display
     */
    String[] getNamedOfRecipeCategories(ArrayList<RecipeCategory> recipeCategories);

    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;
}
