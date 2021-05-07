package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.List;

public interface RecipeSideScrollView {

    void loadingStarted();
    void loadingFailed(String message);

    /**
     * Adds recipes to the existing list of recipes inside the side scrolled list of recipes.
     * @param recipes   List of recipes to add to the display
     */
    void addToList(List<OnlineRecipe> recipes);

    /**
     * Creates a new list of recipes inside the side scrolled list of recipes.
     * @param recipes   List of recipes to display and remove previous display
     */
    void createNewList(List<OnlineRecipe> recipes);

    /**
     * On User selecting a recipe.
     * @param recipe    Online Recipe that was selected
     */
    void onRecipeSelected(OnlineRecipe recipe);
}
