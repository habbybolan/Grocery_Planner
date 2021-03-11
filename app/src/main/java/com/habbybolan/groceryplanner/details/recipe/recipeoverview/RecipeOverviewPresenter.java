package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.List;

public interface RecipeOverviewPresenter {

    void setView(RecipeOverviewView view);
    void destroy();

    void deleteRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);
    /**
     * Loads all recipe categories
     */
    void loadAllRecipeCategories();

    /**
     * Returns all the loaded recipe categories
     * @return  loaded recipe categories
     */
    List<RecipeCategory> getAllCategories();

    /**
     * Call fragment to display recipe categories if possible
     */
    void displayRecipeCategories();

    /**
     * Get the RecipeCategory at index position of the loaded RecipeCategories.
     * @param position  The position in the array of the RecipeCategory
     * @return          The RecipeCategory at position, null if RecipeCategories not loaded in fully
     */
    RecipeCategory getRecipeCategory(int position);

    void fetchRecipeCategory(Long categoryId);

    /**
     * Get the current RecipeCategory name
     * @return  Name of the current recipe's category name if the category exists, empty string otherwise
     */
    String getCurrCategoryName();
}
