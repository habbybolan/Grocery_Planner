package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the recipe list state and retrieved values
 */
public class RecipeListStateImpl<T extends OfflineRecipe> implements RecipeListState<T> {

    private List<T> recipes = new ArrayList<>();
    // Recipe category selected by user.
    private RecipeCategory recipeCategory;
    // Sorting type for the recipe list
    private SortType sortType;
    // List offset for recipes to display.
    private int offset = 0;
    // Number of recipes to display past offset.
    private int size = 10;

    /** Constructor for state with specific size and offset */
    public RecipeListStateImpl(RecipeCategory recipeCategory, SortType sortType, int offset, int size) {
        this.recipeCategory = recipeCategory;
        this.sortType = sortType;
        this.offset = offset;
        this.size = size;
    }

    /** Constructor for state with default size and offset. */
    public RecipeListStateImpl(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        this.sortType = new SortType();
    }

    public List<T> gotoNextPage() {
        if (!canGotoNextPage()) throw new IllegalStateException("Call only if it's possible to go to next page.");
        offset += size;
        return getRecipes();
    }

    @Override
    public boolean canGotoNextPage() {
        return offset + size - 1 <= recipes.size();
    }

    @Override
    public List<T> gotoPreviousPage() {
        if (!canGotoPreviousPage()) throw new IllegalStateException("Call only if it's possible to go to previous page.");
        offset -= size;
        return getRecipes();
    }

    @Override
    public boolean canGotoPreviousPage() {
        return offset > 0;
    }

    @Override
    public void setRecipeList(List<T> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    @Override
    public int getOffset() {
        return offset;
    }
    @Override
    public int getSize() {
        return size;
    }
    @Override
    public SortType getSortType() {
        return sortType;
    }
    @Override
    public void setSortType(int sortTypeKey) {
        this.sortType.setSortType(sortTypeKey);
    }

    @Override
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    @Override
    public void setRecipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        resetOffset();
    }

    private void resetOffset() {
        offset = 0;
    }

    @Override
    public List<T> getRecipes() {
        return recipes;
        /*if (recipes.isEmpty()) return recipes;

        // if there are enough recipe to fit on next page
        if (recipes.size() >= offset + size)
            return recipes.subList(offset, offset+size - 1);
        else
            // otherwise, display the remaining lists
            return recipes.subList(offset, recipes.size()-1);*/
    }
}
