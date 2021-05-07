package com.habbybolan.groceryplanner.ui.recipetagsadapter;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

public interface RecipeTagsView {

    /**
     * Delete the tag from the recipe.
     * @param recipeTag Tag to delete
     */
    void deleteRecipeTag(RecipeTag recipeTag);
}
