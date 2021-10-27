package com.habbybolan.groceryplanner.details.myrecipe;

import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public interface RecipeDetailsInteractor {

    /**
     * Adds a new recipe, or updating an existing recipe.
     * @param myRecipe    Recipe to update or add to database
     */
    void updateRecipe(MyRecipe myRecipe);

    /**
     * Deletes a recipe from the database
     * @param myRecipe    Recipe to delete
     */
    void deleteRecipe(MyRecipe myRecipe);
}
