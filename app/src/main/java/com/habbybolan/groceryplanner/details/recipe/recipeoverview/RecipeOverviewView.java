package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.RecipeCategory;

public interface RecipeOverviewView {
    void loadingStarted();
    void loadingFailed(String message);

    /**
     * Displays the Array of category names in an Alert Dialogue
     * @param categoryNames The Array of category names to display
     */
    void createCategoriesAlertDialogue(String[] categoryNames);

    /**
     * Displays the current RecipeCategory in the fragment
     * @param recipeCategory    RecipeCategory's name to display
     */
    void displayRecipeCategory(RecipeCategory recipeCategory);
}
