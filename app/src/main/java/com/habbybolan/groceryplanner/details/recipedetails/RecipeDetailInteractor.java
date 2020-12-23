package com.habbybolan.groceryplanner.details.recipedetails;

import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeDetailInteractor {

    void editRecipeName(Recipe recipe, String name);
    void deleteRecipe(Recipe recipe);

    /**
     * Get all Ingredient objects associated with Recipe from the database.
     * @param recipe    The ingredient to display
     * @return          All Ingredients associated with recipe
     */
    List<Ingredient> fetchIngredients(Recipe recipe) throws ExecutionException, InterruptedException;
}
