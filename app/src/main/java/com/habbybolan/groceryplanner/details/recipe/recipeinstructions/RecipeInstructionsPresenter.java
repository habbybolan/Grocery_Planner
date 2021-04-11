package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeInstructionsPresenter {

    void setView(RecipeInstructionsView view);
    void destroy();

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
