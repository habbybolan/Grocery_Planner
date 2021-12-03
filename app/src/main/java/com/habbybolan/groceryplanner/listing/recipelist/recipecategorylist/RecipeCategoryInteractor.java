package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeCategoryInteractor {

    void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException;
    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    void addRecipeCategory(RecipeCategory recipeCategory);

    /**
     * Search for the recipe categories with name categorySearch.
     * @param categorySearch   recipe category to search for
     * @param callback         callback to update the list of recipe categories showing
     */
    void searchRecipeCategories(String categorySearch, DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;
}
