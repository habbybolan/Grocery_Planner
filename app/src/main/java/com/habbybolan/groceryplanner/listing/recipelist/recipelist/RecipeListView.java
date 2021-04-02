package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public interface RecipeListView extends ListViewInterface<Recipe> {

    /**
     * Stores the recipe categories inside the presenter to readily display
     * @param recipeCategories  All RecipeCategories stored
     */
    void storeAllRecipeCategories(List<RecipeCategory> recipeCategories);
}
