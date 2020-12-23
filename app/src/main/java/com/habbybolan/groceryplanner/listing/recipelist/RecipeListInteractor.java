package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeListInteractor {

    List<Recipe> fetchRecipes() throws ExecutionException, InterruptedException;
    List<Recipe> searchRecipes(String search);
    void addRecipe(Recipe recipe);
}
