package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractor;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsInteractor extends RecipeDetailsInteractor {

    /**
     * Get all Ingredient objects associated with Recipe from the database.
     * @param offlineRecipe     The ingredient to display
     * @param callback   callback that updates the ingredients feteched
     */
    void fetchIngredients(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

    /**
     * Delete an ingredient from the recipe
     * @param offlineRecipe        The recipe to delete the ingredient from
     * @param ingredient    The ingredient to delete
     */
    void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient);

    /**
     * Delete ingredients from the recipe
     * @param offlineRecipe        The recipe to delete the ingredients from
     * @param ingredients   The ingredients to delete
     */
    void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients);

    /**
     * Search for the recipe ingredients with name ingredientSearch.
     * @param offlineRecipe             recipe to search in for the ingredient
     * @param ingredientSearch   ingredient to search for
     * @param callback           callback to update the list of ingredients showing
     */
    void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
}
