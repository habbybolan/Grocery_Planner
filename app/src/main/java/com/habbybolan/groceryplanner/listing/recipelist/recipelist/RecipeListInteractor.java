package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeListInteractor {

    void fetchRecipes(RecipeCategory recipeCategory, ObservableArrayList<Recipe> recipesObserver) throws ExecutionException, InterruptedException;
    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    List<Recipe> searchRecipes(String search);
    void addRecipe(Recipe recipe);
    void addRecipesToCategory(ArrayList<Recipe>recipes, RecipeCategory category);
    void removeRecipesFromCategory(ArrayList<Recipe> recipes);

    /**
     * Get the ID of the recipe category.
     * @param recipeCategory    Category holding the ID
     * @return                  Recipe SQL category ID. NULL if recipeCategory is null
     */
    Long getCategoryId(RecipeCategory recipeCategory);
    void fetchCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserver) throws ExecutionException, InterruptedException;
}
