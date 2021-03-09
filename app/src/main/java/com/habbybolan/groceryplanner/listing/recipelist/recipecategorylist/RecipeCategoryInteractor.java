package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeCategoryInteractor {

    void fetchRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserver) throws ExecutionException, InterruptedException;
    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    List<Recipe> searchRecipeCategories(String search);
    void addRecipeCategory(RecipeCategory recipeCategory);
}
