package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public interface RecipeOverviewPresenter {

    void setView(RecipeOverviewView view);
    void destroy();

    void deleteRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);
    /**
     * Loads all recipe categories
     */
    void loadAllRecipeCategories();

    /**
     * Returns all the loaded recipe categories
     * @return  loaded recipe categories
     */
    List<RecipeCategory> getAllCategories();

    /**
     * Called display recipe categories if possible
     */
    void displayRecipeCategories();

    /**
     * Get the RecipeCategory at index position of the loaded RecipeCategories.
     * @param position  The position in the array of the RecipeCategory
     * @return          The RecipeCategory at position, null if the last position was selected (No Category)
     */
    RecipeCategory getRecipeCategory(int position);

    /**
     * Fetch the category from the database to display.
     * @param categoryId    The Id of the category to display
     */
    void fetchRecipeCategory(Long categoryId);

    /**
     * Get the current RecipeCategory name
     * @return  Name of the current recipe's category name if the category exists, empty string otherwise
     */
    String getCurrCategoryName();

    /**
     * Fetch the Groceries that are holding the recipe.
     * @param recipe    The recipe begin contained by the Grocery object
     */
    void fetchGroceriesHoldingRecipe(Recipe recipe);

    /**
     * Fetch the Groceries that are not holding the recipe.
     * @param recipe    The recipe not contained by the Grocery object
     */
    void fetchGroceriesNotHoldingRecipe(Recipe recipe);

    /**
     * Called to display the Groceries not holding the Recipe
     */
    void displayGroceriesNotHoldingRecipe();

    /**
     * Adds the recipe to the grocery list.
     * @param recipe        Recipe to add to grocery
     * @param grocery       grocery to hold the recipe
     * @param amount        The number of times to add the Recipe to the grocery
     * @param ingredients   recipe ingredients to add or remove from the grocery list
     */
    void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients);

    /**
     * Fetches the recipe ingredients where the recipe is not part of the selected grocery list.
     * @param recipe            The recipe to add/change inside the grocery list
     * @param grocery           The grocery list to hold/is holding the recipe ingredients
     * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
     */
    void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery);

    /**
     * Returns an Array of all the Grocery names in groceries
     * @param groceries The list to extract the String names from
     * @return          Array of names of the groceries
     */
    String[] getArrayOfGroceryNames(List<Grocery> groceries);

    /**
     * Delete All the recipe ingredients from the grocery
     * @param recipe    Recipe to delete from grocery
     * @param grocery   Grocery holding recipe and its ingredients to delete
     */
    void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery);
}
