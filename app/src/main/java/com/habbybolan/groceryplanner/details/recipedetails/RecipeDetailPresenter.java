package com.habbybolan.groceryplanner.details.recipedetails;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Recipe;

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
     * Get all Ingredient objects associated with recipe from the database.
     * @param recipe   The recipe associated with the Ingredients to display
     */
    void createIngredientList(Recipe recipe);
}
