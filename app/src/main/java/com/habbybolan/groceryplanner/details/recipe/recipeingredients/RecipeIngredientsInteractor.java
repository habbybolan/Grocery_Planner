package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractor;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsInteractor extends RecipeDetailsInteractor {

    /**
     * Get all Ingredient objects associated with Recipe from the database.
     * @param recipe     The ingredient to display
     * @param callback   callback that updates the ingredients feteched
     */
    void fetchIngredients(Recipe recipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

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

    /**
     * Search for the recipe ingredients with name ingredientSearch.
     * @param recipe             recipe to search in for the ingredient
     * @param ingredientSearch   ingredient to search for
     * @param callback           callback to update the list of ingredients showing
     */
    void searchIngredients(Recipe recipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
}
