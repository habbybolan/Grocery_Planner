package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

/**
 * Holds the current page state to know which page user is currently on.
 */
public interface RecipeListState {

    /**
     * Change the offset to represent going to next page.
     */
    List<Recipe> gotoNextPage() throws IllegalStateException;

    /**
     * @return  true if not at the end of the list and can display more on another page.
     */
    boolean canGotoNextPage();

    /**
     * Change the offset to represent going back to the previous page.
     */
    List<Recipe> gotoPreviousPage() throws IllegalStateException;

    /**
     * @return  True if user can go back a page, false otherwise.
     */
    boolean canGotoPreviousPage();

    /**
     * Sets the Recipe list.
     * @param recipes   List of recipes to store.
     */
    void setRecipeList(List<Recipe> recipes);

    int getOffset();
    int getSize();
    RecipeCategory getRecipeCategory();
    List<Recipe> getRecipes();
}
