package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

public interface RecipeOverviewPresenter {

    void setView(RecipeOverviewView view);
    void destroy();

    void deleteRecipe(OfflineRecipe offlineRecipe);

    void updateRecipe(OfflineRecipe offlineRecipe);
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
     * @param offlineRecipe    The recipe begin contained by the Grocery object
     */
    void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe);

    /**
     * Fetch the Groceries that are not holding the recipe.
     * @param offlineRecipe    The recipe not contained by the Grocery object
     */
    void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe);

    /**
     * Called to display the Groceries not holding the Recipe
     */
    void displayGroceriesNotHoldingRecipe();

    /**
     * Adds the recipe to the grocery list.
     * @param offlineRecipe        Recipe to add to grocery
     * @param grocery       grocery to hold the recipe
     * @param amount        The number of times to add the Recipe to the grocery
     * @param ingredients   recipe ingredients to add or remove from the grocery list
     */
    void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients);

    /**
     * Fetches the recipe ingredients where the recipe is not part of the selected grocery list.
     * @param offlineRecipe            The recipe to add/change inside the grocery list
     * @param grocery           The grocery list to hold/is holding the recipe ingredients
     * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
     */
    void fetchRecipeIngredients(OfflineRecipe offlineRecipe, Grocery grocery, boolean isNotInGrocery);

    /**
     * Returns an Array of all the Grocery names in groceries
     * @param groceries The list to extract the String names from
     * @return          Array of names of the groceries
     */
    String[] getArrayOfGroceryNames(List<Grocery> groceries);

    /**
     * Delete All the recipe ingredients from the grocery
     * @param offlineRecipe    Recipe to delete from grocery
     * @param grocery   Grocery holding recipe and its ingredients to delete
     */
    void deleteRecipeFromGrocery(OfflineRecipe offlineRecipe, Grocery grocery);

    /**
     * create the list of recipe tags from the database.
     * @param offlineRecipe    Recipe holding the tags
     */
    void createRecipeTagList(OfflineRecipe offlineRecipe);

    /**
     * Delete the tag from the recipe
     * @param offlineRecipe    recipe holding the tag
     * @param recipeTag tag to delete from the recipe
     */
    void deleteRecipeTag(OfflineRecipe offlineRecipe, RecipeTag recipeTag);

    /**
     * Checks if the the name for the new RecipeTag is valid.
     * Adds the new tag if valid, otherwise send back error message.
     * @param title      Title of the new RecipeTag being added
     * @param offlineRecipe    Recipe the tag will be added to
     * @param recipeTags List of RecipeTag to add the new RecipeTag to
     */
    void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, OfflineRecipe offlineRecipe);
}
