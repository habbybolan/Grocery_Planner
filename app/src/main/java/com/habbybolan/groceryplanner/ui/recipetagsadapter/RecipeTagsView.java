package com.habbybolan.groceryplanner.ui.recipetagsadapter;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

public interface RecipeTagsView {

    /**
     * Recipe tag removed from the list of RecipeTag
     * @param recipeTag Tag to delete
     */
    void onDeleteRecipeTag(RecipeTag recipeTag);
}
