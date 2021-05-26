package com.habbybolan.groceryplanner.details.recipe;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeDetailsInteractor {

    /**
     * Adds a new recipe, or updating an existing recipe.
     * @param offlineRecipe    Recipe to update or add to database
     */
    void updateRecipe(OfflineRecipe offlineRecipe);

    /**
     * Deletes a recipe from the database
     * @param offlineRecipe    Recipe to delete
     */
    void deleteRecipe(OfflineRecipe offlineRecipe);
}
