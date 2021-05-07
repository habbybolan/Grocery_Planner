package com.habbybolan.groceryplanner.MainPage.recipesnippet;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

public interface RecipeSnippetInteractor {

    /**
     * User selected for the recipe to be saved online to their saved recipes.
     * @param recipe    Recipe to save to user's online saved recipes
     */
    void onSaveRecipeToOnline(OnlineRecipe recipe);

    /**
     * User selected for the recipe to be saved to the offline database.
     * @param recipe    Recipe to be added to the Room database
     */
    void onSaveRecipeOffline(OnlineRecipe recipe);
}
