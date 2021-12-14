package com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AddRecipeToGroceryListContract {

    interface Presenter {

        void destroy();

        void setView(View view);

        void setRecipe(OfflineRecipe recipe);

        void updateRecipeIngredientsInGrocery(Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients);


            /**
             * Fetches the recipe ingredients where the recipe is not part of the selected grocery list.
             * @param grocery           The grocery list to hold/is holding the recipe ingredients
             * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
             */
        void fetchRecipeIngredients(Grocery grocery, boolean isNotInGrocery);

        /**
         * Fetch the Groceries that are holding the recipe.
         */
        void fetchGroceriesHoldingRecipe();

        List<GroceryRecipe> getLoadedGroceriesHoldingRecipe();

        /**
         * Fetch the Groceries that are not holding the recipe.
         */
        void fetchGroceriesNotHoldingRecipe();

        /**
         * Called to display the Groceries not holding the Recipe
         */
        void displayGroceriesNotHoldingRecipe();

        /**
         * Returns an Array of all the Grocery names in groceries
         * @param groceries The list to extract the String names from
         * @return          Array of names of the groceries
         */
        String[] getArrayOfGroceryNames(List<Grocery> groceries);

        /**
         * Delete All the recipe ingredients from the grocery
         * @param grocery   Grocery holding recipe and its ingredients to delete
         */
        void deleteRecipeFromGrocery(Grocery grocery);

        OfflineRecipe getRecipe();

    }

    interface Interactor {

        /**
         * Fetch the Groceries that are holding the recipe.
         * @param recipe            The recipe begin contained by the Grocery object
         * @param callback          callback to update the Groceries with the amount of the recipe fetched
         */
        void fetchGroceriesHoldingRecipe(OfflineRecipe recipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException;


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
        void fetchGroceriesNotHoldingRecipe(OfflineRecipe myRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;


        /**
         * Adds the recipe to the grocery list.
         * @param myRecipe            Recipe to add to grocery
         * @param grocery           grocery to hold the recipe
         * @param amount            The number of times to add the Recipe to the grocery
         * @param recipeIngredients recipe ingredients to add or remove from the grocery list
         */
        void updateRecipeIngredientsInGrocery(OfflineRecipe myRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);


        /**
         * Delete All the recipe ingredients from the grocery
         * @param myRecipe    Recipe to delete from grocery
         * @param grocery   Grocery holding recipe and its ingredients to delete
         */
        void deleteRecipeFromGrocery(OfflineRecipe myRecipe, Grocery grocery);

        /**
         * Fetches the recipe ingredients that will be or are already added to a grocery list through the recipe.
         * @param myRecipe            The recipe to add/change inside the grocery list
         * @param grocery           The grocery list to hold/is holding the recipe ingredients
         * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
         * @param callback          Callback to update the recipe ingredients to with a check value to show
         *                          if it is added to the grocery list.
         */
        void fetchRecipeIngredients(OfflineRecipe myRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

        /**
         * Get an array of the ingredient names.
         * @param ingredients   Ingredients with the names
         * @return              Array of ingredient names
         */
        String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients);

        /**
         * Returns an Array of all the Grocery names in groceries
         * @param groceries The list to extract the String names from
         * @return          Array of names of the groceries
         */
        String[] getArrayOfGroceryNames(List<Grocery> groceries);
    }

    interface View {

        void loadingStarted();
        void loadingFailed(String message);

        /**
         * Displays the Groceries that are holding the current Recipe.
         */
        void displayGroceriesHoldingRecipe();

        /**
         * Display the ingredients that are added and not added to the Grocery list through the current Recipe.
         * @param grocery   The Grocery holding the ingredients
         */
        void onGroceryHoldingRecipeClicked(Grocery grocery);

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
    }
}
