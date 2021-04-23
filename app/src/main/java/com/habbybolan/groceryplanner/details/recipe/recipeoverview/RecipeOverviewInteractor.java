package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractor;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewInteractor extends RecipeDetailsInteractor {

    void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

    /**
     * Select all ingredients if all unselected, otherwise do nothing.
     * Used for when a recipe is being added to a grocery, all ingredients should be selected at first.
     * @param ingredients   Ingredients holding a isInGrocery value
     * @return              Fully checked ingredients list, or an unchanged list
     */
    List<IngredientWithGroceryCheck> checkIfAllUnselected(List<IngredientWithGroceryCheck> ingredients);

    /**
     * Converts the list of RecipeCategory to an array of their names
     * @param recipeCategories  loaded recipe categories
     * @return                  Array of the loaded RecipeCategory names to display
     */
    String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories);

    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Groceries that are holding the recipe.
     * @param recipe            The recipe begin contained by the Grocery object
     * @param callback          callback to update the Groceries with the amount of the recipe fetched
     */
    void fetchGroceriesHoldingRecipe(Recipe recipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Groceries that are not holding the recipe.
     * @param recipe    The recipe not contained by the Grocery object
     * @param callback  callback to update the Groceries not holding the recipe
     */
    void fetchGroceriesNotHoldingRecipe(Recipe recipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;

    /**
     * Adds the recipe to the grocery list.
     * @param recipe            Recipe to add to grocery
     * @param grocery           grocery to hold the recipe
     * @param amount            The number of times to add the Recipe to the grocery
     * @param recipeIngredients recipe ingredients to add or remove from the grocery list
     */
    void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);

    /**
     * Returns an Array of all the Grocery names in groceries
     * @param groceries The list to extract the String names from
     * @return          Array of names of the groceries
     */
    String[] getArrayOfGroceryNames(List<Grocery> groceries);

    /**
     * Get an array of the ingredient names.
     * @param ingredients   Ingredients with the names
     * @return              Array of ingredient names
     */
    String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients);

    /**
     * Fetches the recipe ingredients that will be or are already added to a grocery list through the recipe.
     * @param recipe            The recipe to add/change inside the grocery list
     * @param grocery           The grocery list to hold/is holding the recipe ingredients
     * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
     * @param callback          Callback to update the recipe ingredients to with a check value to show
     *                          if it is added to the grocery list.
     */
    void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

    /**
     * Delete All the recipe ingredients from the grocery
     * @param recipe    Recipe to delete from grocery
     * @param grocery   Grocery holding recipe and its ingredients to delete
     */
    void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery);

    /**
     * Adds a tag to the recipe.
     * @param title     title of the tag
     * @param recipe    recipe to place the tag into
     */
    void addTag(Recipe recipe, String title);

    /**
     * Fetch all recipe tags associated to recipe.
     * @param recipe    Recipe holding the tags to fetch
     * @param callback  callback for updating recipe tags fetched
     */
    void fetchTags(Recipe recipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException;

    /**
     * Delete the tag from the recipe
     * @param recipe    recipe holding the tag
     * @param recipeTag tag to delete from the recipe
     */
    void deleteRecipeTag(Recipe recipe, RecipeTag recipeTag);

    /**
     * Check if the tag title is valid.
     * @param title title of the tag to check
     * @return      True if the tag title is valid, false otherwise.
     */
    boolean isTagTitleValid(String title);

    /**
     * Reformat the tag title so it is in Camel Case, no leading zeros, single space zeros, and single occurring dashes.
     * @param title Tag title to reformat if needed.
     * @return      The reformatted tag title String
     */
    String reformatTagTitle(String title);
}
