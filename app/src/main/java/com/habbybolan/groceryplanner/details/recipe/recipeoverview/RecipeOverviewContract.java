package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewContract {

    interface Presenter {
        void setView(OverviewView view);
        void destroy();

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

        /** Calls Interactor {@link RecipeOverviewContract.Interactor#updateRecipe} to update the recipe values changed. */
        void updateRecipe(OfflineRecipe offlineRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    interface Interactor {
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
         * @param offlineRecipe            The recipe begin contained by the Grocery object
         * @param callback          callback to update the Groceries with the amount of the recipe fetched
         */
        void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException;

        /**
         * Fetch the Groceries that are not holding the recipe.
         * @param offlineRecipe    The recipe not contained by the Grocery object
         * @param callback  callback to update the Groceries not holding the recipe
         */
        void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;

        /**
         * Adds the recipe to the grocery list.
         * @param offlineRecipe            Recipe to add to grocery
         * @param grocery           grocery to hold the recipe
         * @param amount            The number of times to add the Recipe to the grocery
         * @param recipeIngredients recipe ingredients to add or remove from the grocery list
         */
        void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);

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
         * @param offlineRecipe            The recipe to add/change inside the grocery list
         * @param grocery           The grocery list to hold/is holding the recipe ingredients
         * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
         * @param callback          Callback to update the recipe ingredients to with a check value to show
         *                          if it is added to the grocery list.
         */
        void fetchRecipeIngredients(OfflineRecipe offlineRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

        /**
         * Delete All the recipe ingredients from the grocery
         * @param offlineRecipe    Recipe to delete from grocery
         * @param grocery   Grocery holding recipe and its ingredients to delete
         */
        void deleteRecipeFromGrocery(OfflineRecipe offlineRecipe, Grocery grocery);

        /**
         * Adds a tag to the recipe.
         * @param recipeTags List of RecipeTag to add the new RecipeTag to
         * @param title     title of the tag
         * @param offlineRecipe    recipe to place the tag into
         * @return          True if the recipe tag addition is valid, otherwise false
         */
        boolean addTag(List<RecipeTag> recipeTags, OfflineRecipe offlineRecipe, String title);

        /**
         * Fetch all recipe tags associated to recipe.
         * @param offlineRecipe    Recipe holding the tags to fetch
         * @param callback  callback for updating recipe tags fetched
         */
        void fetchTags(OfflineRecipe offlineRecipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException;

        /**
         * Delete the tag from the recipe
         * @param offlineRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(OfflineRecipe offlineRecipe, RecipeTag recipeTag);

        /**
         * Updates the OfflineRecipe values based on user set values and save the changes to room database.
         * @param offlineRecipe     OfflineRecipe to set new values to
         * @param name              Name of recipe to set.
         * @param servingSize       Serving size to set
         * @param cookTime          cook time to set
         * @param prepTime          prep time to set
         * @param description       description to set
         * @param category          category to set
         */
        void updateRecipe(OfflineRecipe offlineRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    interface OverviewView extends RecipeTagsView {
        void loadingStarted();
        void loadingFailed(String message);

        /**
         * Displays the Array of category names in an Alert Dialogue
         * @param categoryNames The Array of category names to display
         */
        void createCategoriesAlertDialogue(String[] categoryNames);

        /**
         * Displays the current RecipeCategory in the fragment
         * @param recipeCategory    RecipeCategory's name to display
         */
        void displayRecipeCategory(RecipeCategory recipeCategory);

        /**
         * Displays the Groceries that are holding the current Recipe.
         * @param groceries The Groceries holding the Recipe to display
         */
        void displayGroceriesHoldingRecipe(List<GroceryRecipe> groceries);

        /**
         * Displays ingredients, checked if they will be added /  already added to Grocery list
         * @param ingredients       ArrayList of ingredients objects that can be selected
         * @param ingredientNames   Array of Ingredient names to display in list.
         * @param grocery           Grocery list to add the recipe ingredients to
         */
        void displayRecipeIngredients(List<IngredientWithGroceryCheck> ingredients, String[] ingredientNames, Grocery grocery);

        /**
         * Displays the Groceries not holding the recipes inside a Dialog.
         * @param groceries Groceries to display not holding the recipe
         */
        void displayGroceriesNotHoldingRecipe(List<Grocery> groceries);

        /**
         * Display the ingredients that are added and not added to the Grocery list through the current Recipe.
         * @param grocery   The Grocery holding the ingredients
         */
        void displayRecipeIngredientsInGrocery(Grocery grocery);

        /**
         * Display the recipe tags.
         * @param recipeTags    List of recipe tags to display.
         */
        void displayRecipeTags(List<RecipeTag> recipeTags);

        /**
         * Update the changes to the RecipeTag list.
         */
        void updateTagDisplay();
    }
}
