package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public interface RecipeCategoryPresenter {

    void destroy();
    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    void addRecipeCategory(RecipeCategory recipeCategory);
    void setView(RecipeCategoryView view);
    void fetchRecipeCategories();

    /**
     * Search for the recipe categories with name categorySearch.
     * @param categorySearch   recipe category to search for
     */
    void searchRecipeCategories(String categorySearch);
}
