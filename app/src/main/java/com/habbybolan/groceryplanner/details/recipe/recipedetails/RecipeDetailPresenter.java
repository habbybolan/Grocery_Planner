package com.habbybolan.groceryplanner.details.recipe.recipedetails;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;

public interface RecipeDetailPresenter {

    void setView(ListViewInterface view);
    void destroy();

    /**
     * Edits the Recipe name.
     * @param recipe    Recipe to edit
     * @param name      New name to set
     */
    void editRecipeName(Recipe recipe, String name);

    /**
     * Recipe to delete.
     * @param recipe   The recipe to delete.
     */
    void deleteRecipe(Recipe recipe);

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
     * Get all Ingredient objects associated with recipe from the database.
     * @param recipe   The recipe associated with the Ingredients to display
     */
    void createIngredientList(Recipe recipe);
}
