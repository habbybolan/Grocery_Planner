package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;

/**
 * Holds the current page state to know which page user is currently on.
 */
public interface RecipeListState<T extends OfflineRecipe> {

    /**
     * Change the offset to represent going to next page.
     */
    List<T> gotoNextPage() throws IllegalStateException;

    /**
     * @return  true if not at the end of the list and can display more on another page.
     */
    boolean canGotoNextPage();

    /**
     * Change the offset to represent going back to the previous page.
     */
    List<T> gotoPreviousPage() throws IllegalStateException;

    /**
     * @return  True if user can go back a page, false otherwise.
     */
    boolean canGotoPreviousPage();

    /**
     * Sets the Recipe list.
     * @param recipes   List of recipes to store.
     */
    void setRecipeList(List<T> recipes);

    int getOffset();
    int getSize();
    RecipeCategory getRecipeCategory();
    void setRecipeCategory(RecipeCategory recipeCategory);
    List<T> getRecipes();

    public SortType getSortType();
    public void setSortType(int sortTypeKey);
}
