package com.habbybolan.groceryplanner.details.myrecipe.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewContract {

    interface Presenter<T extends OverviewView> {

        void destroy();

        void setView(T view);

        /**
         * Get the RecipeCategory at index position of the loaded RecipeCategories.
         * @param position  The position in the array of the RecipeCategory
         * @return          The RecipeCategory at position, null if the last position was selected (No Category)
         */
        RecipeCategory getRecipeCategory(int position);

        /**
         * create the list of recipe tags from the database.
         * @param myRecipe    Recipe holding the tags
         */
        void createRecipeTagList(MyRecipe myRecipe);

        /**
         * Fetch the category from the database to display.
         * @param categoryId    The Id of the category to display
         */
        void fetchRecipeCategory(Long categoryId);

        /**
         * Fetch the Groceries that are holding the recipe.
         * @param myRecipe    The recipe begin contained by the Grocery object
         */
        void fetchGroceriesHoldingRecipe(MyRecipe myRecipe);

        List<RecipeTag> getLoadedRecipeTags();

        List<GroceryRecipe> getLoadedGroceriesHoldingRecipe();
    }

    interface PresenterEdit extends Presenter<OverviewEditView> {

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
         * Fetch the Groceries that are not holding the recipe.
         * @param myRecipe    The recipe not contained by the Grocery object
         */
        void fetchGroceriesNotHoldingRecipe(MyRecipe myRecipe);

        /**
         * Called to display the Groceries not holding the Recipe
         */
        void displayGroceriesNotHoldingRecipe();

        /**
         * Adds the recipe to the grocery list.
         * @param myRecipe        Recipe to add to grocery
         * @param grocery       grocery to hold the recipe
         * @param amount        The number of times to add the Recipe to the grocery
         * @param ingredients   recipe ingredients to add or remove from the grocery list
         */
        void updateRecipeIngredientsInGrocery(MyRecipe myRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients);

        /**
         * Fetches the recipe ingredients where the recipe is not part of the selected grocery list.
         * @param myRecipe            The recipe to add/change inside the grocery list
         * @param grocery           The grocery list to hold/is holding the recipe ingredients
         * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
         */
        void fetchRecipeIngredients(MyRecipe myRecipe, Grocery grocery, boolean isNotInGrocery);

        /**
         * Returns an Array of all the Grocery names in groceries
         * @param groceries The list to extract the String names from
         * @return          Array of names of the groceries
         */
        String[] getArrayOfGroceryNames(List<Grocery> groceries);

        /**
         * Delete All the recipe ingredients from the grocery
         * @param myRecipe    Recipe to delete from grocery
         * @param grocery   Grocery holding recipe and its ingredients to delete
         */
        void deleteRecipeFromGrocery(MyRecipe myRecipe, Grocery grocery);

        /**
         * Delete the tag from the recipe
         * @param myRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(MyRecipe myRecipe, RecipeTag recipeTag);

        /**
         * Checks if the the name for the new RecipeTag is valid.
         * Adds the new tag if valid, otherwise send back error message.
         * @param title      Title of the new RecipeTag being added
         * @param myRecipe    Recipe the tag will be added to
         * @param recipeTags List of RecipeTag to add the new RecipeTag to
         */
        void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, MyRecipe myRecipe);

        /** Calls Interactor to update the recipe values changed. */
        void updateRecipe(MyRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly extends Presenter<OverviewView> {}

    interface Interactor {

        void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;

        /**
         * Fetch the Groceries that are holding the recipe.
         * @param myRecipe            The recipe begin contained by the Grocery object
         * @param callback          callback to update the Groceries with the amount of the recipe fetched
         */
        void fetchGroceriesHoldingRecipe(MyRecipe myRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException;




        /**
         * Fetch all recipe tags associated to recipe.
         * @param myRecipe    Recipe holding the tags to fetch
         * @param callback  callback for updating recipe tags fetched
         */
        void fetchTags(MyRecipe myRecipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException;


    }

    interface InteractorEdit extends Interactor {

        void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

        /**
         * Returns an Array of all the Grocery names in groceries
         * @param groceries The list to extract the String names from
         * @return          Array of names of the groceries
         */
        String[] getArrayOfGroceryNames(List<Grocery> groceries);

        /**
         * Converts the list of RecipeCategory to an array of their names
         * @param recipeCategories  loaded recipe categories
         * @return                  Array of the loaded RecipeCategory names to display
         */
        String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories);

        /**
         * Select all ingredients if all unselected, otherwise do nothing.
         * Used for when a recipe is being added to a grocery, all ingredients should be selected at first.
         * @param ingredients   Ingredients holding a isInGrocery value
         * @return              Fully checked ingredients list, or an unchanged list
         */
        List<IngredientWithGroceryCheck> checkIfAllUnselected(List<IngredientWithGroceryCheck> ingredients);

        /**
         * Fetch the Groceries that are not holding the recipe.
         * @param myRecipe    The recipe not contained by the Grocery object
         * @param callback  callback to update the Groceries not holding the recipe
         */
        void fetchGroceriesNotHoldingRecipe(MyRecipe myRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;

        /**
         * Adds the recipe to the grocery list.
         * @param myRecipe            Recipe to add to grocery
         * @param grocery           grocery to hold the recipe
         * @param amount            The number of times to add the Recipe to the grocery
         * @param recipeIngredients recipe ingredients to add or remove from the grocery list
         */
        void updateRecipeIngredientsInGrocery(MyRecipe myRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);

        /**
         * Get an array of the ingredient names.
         * @param ingredients   Ingredients with the names
         * @return              Array of ingredient names
         */
        String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients);

        /**
         * Fetches the recipe ingredients that will be or are already added to a grocery list through the recipe.
         * @param myRecipe            The recipe to add/change inside the grocery list
         * @param grocery           The grocery list to hold/is holding the recipe ingredients
         * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
         * @param callback          Callback to update the recipe ingredients to with a check value to show
         *                          if it is added to the grocery list.
         */
        void fetchRecipeIngredients(MyRecipe myRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

        /**
         * Delete All the recipe ingredients from the grocery
         * @param myRecipe    Recipe to delete from grocery
         * @param grocery   Grocery holding recipe and its ingredients to delete
         */
        void deleteRecipeFromGrocery(MyRecipe myRecipe, Grocery grocery);

        /**
         * Adds a tag to the recipe.
         * @param recipeTags List of RecipeTag to add the new RecipeTag to
         * @param title     title of the tag
         * @param myRecipe    recipe to place the tag into
         * @return          True if the recipe tag addition is valid, otherwise false
         */
        boolean addTag(List<RecipeTag> recipeTags, MyRecipe myRecipe, String title);

        /**
         * Delete the tag from the recipe
         * @param myRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(MyRecipe myRecipe, RecipeTag recipeTag);

        /**
         * Updates the MyRecipe values based on user set values and save the changes to room database.
         * @param myRecipe     MyRecipe to set new values to
         * @param name              Name of recipe to set.
         * @param servingSize       Serving size to set
         * @param cookTime          cook time to set
         * @param prepTime          prep time to set
         * @param description       description to set
         * @param category          category to set
         */
        void updateRecipe(MyRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    interface OverviewView extends RecipeTagsView {

        void loadingStarted();
        void loadingFailed(String message);

        /**
         * Displays the Groceries that are holding the current Recipe.
         */
        void displayGroceriesHoldingRecipe();

        /**
         * Displays the current RecipeCategory in the fragment
         * @param recipeCategory    RecipeCategory's name to display
         */
        void displayRecipeCategory(RecipeCategory recipeCategory);

        /**
         * Display the recipe tags.
         */
        void displayRecipeTags();

        /**
         * Display the ingredients that are added and not added to the Grocery list through the current Recipe.
         * @param grocery   The Grocery holding the ingredients
         */
        void onGroceryHoldingRecipeClicked(Grocery grocery);
    }

    interface OverviewEditView extends OverviewView {

        /**
         * Displays the Array of category names in an Alert Dialogue
         * @param categoryNames The Array of category names to display
         */
        void createCategoriesAlertDialogue(String[] categoryNames);

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
         * Update the changes to the RecipeTag list.
         */
        void updateTagDisplay();
    }

    interface RecipeOverviewListener {
        MyRecipe getMyRecipe();
        void onSwapViewOverview();
    }
}
