package com.habbybolan.groceryplanner.details.recipe;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeDetailsInteractor {

    /**
     * Adds a new recipe, or updating an existing recipe.
     * @param recipe    Recipe to update or add to database
     */
    void updateRecipe(Recipe recipe);

    /**
     * Deletes a recipe from the database
     * @param recipe    Recipe to delete
     */
    void deleteRecipe(Recipe recipe);
}
