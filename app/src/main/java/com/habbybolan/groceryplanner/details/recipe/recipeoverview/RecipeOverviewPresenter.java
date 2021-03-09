package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.List;

public interface RecipeOverviewPresenter {

    void setView(RecipeOverviewView view);
    void destroy();

    void deleteRecipe(Recipe recipe);

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
     * Call fragments method to display recipe categories if possible
     */
    void displayRecipeCategories();
}
