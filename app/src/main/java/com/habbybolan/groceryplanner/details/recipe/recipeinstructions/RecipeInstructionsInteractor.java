package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeInstructionsInteractor {

    /**
     * update the recipe with the new instructions
     * @param offlineRecipe    Recipe to update
     */
    void updateRecipe(OfflineRecipe offlineRecipe);

    /**
     * Delete the recipe from the database
     * @param offlineRecipe    Recipe to delete
     */
    void deleteRecipe(OfflineRecipe offlineRecipe);
}
