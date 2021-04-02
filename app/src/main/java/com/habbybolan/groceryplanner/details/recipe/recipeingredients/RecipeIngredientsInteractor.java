package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsInteractor {

    void editRecipeName(Recipe recipe, String name);
    void deleteRecipe(Recipe recipe);

    /**
     * Get all Ingredient objects associated with Recipe from the database.
     * @param recipe                The ingredient to display
     * @param ingredientsObserver   Ingredients observer that holds the fetched ingredients
     */
    void fetchIngredients(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;

    /**
     * Delete an ingredient from the recipe
     * @param recipe        The recipe to delete the ingredient from
     * @param ingredient    The ingredient to delete
     */
    void deleteIngredient(Recipe recipe, Ingredient ingredient);

    /**
     * Delete ingredients from the recipe
     * @param recipe        The recipe to delete the ingredients from
     * @param ingredients   The ingredients to delete
     */
    void deleteIngredients(Recipe recipe, List<Ingredient> ingredients);
}
