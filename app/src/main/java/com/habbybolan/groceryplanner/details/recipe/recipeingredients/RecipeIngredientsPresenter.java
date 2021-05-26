package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;

public interface RecipeIngredientsPresenter {

    void setView(ListViewInterface view);
    void destroy();

    /**
     * Edits the Recipe name.
     * @param offlineRecipe    Recipe to edit
     * @param name      New name to set
     */
    void editRecipeName(OfflineRecipe offlineRecipe, String name);

    /**
     * Recipe to delete.
     * @param offlineRecipe   The recipe to delete.
     */
    void deleteRecipe(OfflineRecipe offlineRecipe);

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
     * Get all Ingredient objects associated with recipe from the database.
     * @param offlineRecipe   The recipe associated with the Ingredients to display
     */
    void createIngredientList(OfflineRecipe offlineRecipe);

    /**
     * Search for the recipe ingredients with name ingredientSearch.
     * @param offlineRecipe             recipe to search in for the ingredient
     * @param ingredientSearch   ingredient to search for
     */
    void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch);
}
