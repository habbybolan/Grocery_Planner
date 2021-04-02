package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public interface RecipeCategoryPresenter {

    void destroy();
    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    void addRecipeCategory(RecipeCategory recipeCategory);
    void setView(ListViewInterface view);
    void fetchRecipeCategories();
}
