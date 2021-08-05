package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the recipe list state and retrieved values
 */
public class RecipeListStateImpl implements RecipeListState {

    private List<Recipe> recipes = new ArrayList<>();
    // Recipe category selected by user.
    private final RecipeCategory recipeCategory;
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
    }

    public List<Recipe> gotoNextPage() {
        if (!canGotoNextPage()) throw new IllegalStateException("Call only if it's possible to go to next page.");
        offset += size;
        return getRecipes();
    }

    @Override
    public boolean canGotoNextPage() {
        return offset + size - 1 <= recipes.size();
    }

    @Override
    public List<Recipe> gotoPreviousPage() {
        if (!canGotoPreviousPage()) throw new IllegalStateException("Call only if it's possible to go to previous page.");
        offset -= size;
        return getRecipes();
    }

    @Override
    public boolean canGotoPreviousPage() {
        return offset > 0;
    }

    @Override
    public void setRecipeList(List<Recipe> recipes) {
        // todo;
    }

    public int getOffset() {
        return offset;
    }
    public int getSize() {
        return size;
    }
    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(int sortTypeKey) {
        this.sortType.setSortType(sortTypeKey);
    }

    @Override
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    @Override
    public List<Recipe> getRecipes() {
        if (recipes.size() >= offset + size - 1)
            return recipes.subList(offset, offset+size - 1);
        else
            return recipes.subList(offset, recipes.size()-1);
    }
}
