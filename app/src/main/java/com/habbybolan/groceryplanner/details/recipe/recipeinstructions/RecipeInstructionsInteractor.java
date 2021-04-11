package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeInstructionsInteractor {

    /**
     * update the recipe with the new instructions
     * @param recipe    Recipe to update
     */
    void updateRecipe(Recipe recipe);

    /**
     * Delete the recipe from the database
     * @param recipe    Recipe to delete
     */
    void deleteRecipe(Recipe recipe);
}
