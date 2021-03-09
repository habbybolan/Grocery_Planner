package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

public interface RecipeOverviewView {
    void loadingStarted();
    void loadingFailed(String message);

    /**
     * Displays the Array of category names in an Alert Dialogue
     * @param categoryNames The Array of category names to display
     */
    void createCategoriesAlertDialogue(String[] categoryNames);
}
