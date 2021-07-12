package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;

public class RecipeListState implements RecipeListContract.State {

    private List<Recipe> recipes = new ArrayList<>();
    // Recipe category selected by user.
    private final RecipeCategory recipeCategory;
    // List offset for recipes to display.
    private int offset = 0;
    // Number of recipes to display past offset.
    private int size = 10;

    /** Constructor for state with specific size and offset */
    public RecipeListState(RecipeCategory recipeCategory, int offset, int size) {
        this.recipeCategory = recipeCategory;
        this.offset = offset;
        this.size = size;
    }

    /** Constructor for state with default size and offset. */
    public RecipeListState(RecipeCategory recipeCategory) {
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
