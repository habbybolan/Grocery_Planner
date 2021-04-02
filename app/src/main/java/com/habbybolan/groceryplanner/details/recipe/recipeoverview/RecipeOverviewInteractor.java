package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewInteractor {

    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);


    void loadAllRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException;

    /**
     * Converts the list of RecipeCategory to an array of their names
     * @param recipeCategories  loaded recipe categories
     * @return                  Array of the loaded RecipeCategory names to display
     */
    String[] getNamedOfRecipeCategories(ArrayList<RecipeCategory> recipeCategories);

    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Groceries that are holding the recipe.
     * @param recipe            The recipe begin contained by the Grocery object
     * @param groceriesObserver Observer to store the Groceries with the amount of the recipe is in it
     */
    void fetchGroceriesHoldingRecipe(Recipe recipe, ObservableArrayList<GroceryRecipe> groceriesObserver) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Groceries that are not holding the recipe.
     * @param recipe            The recipe not contained by the Grocery object
     * @param groceriesObserver Observer to store the Groceries not holding the recipe
     */
    void fetchGroceriesNotHoldingRecipe(Recipe recipe, ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException;

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
     * Fetches the
     * @param recipe            The recipe to add/change inside the grocery list
     * @param grocery           The grocery list to hold/is holding the recipe ingredients
     * @param isNotInGrocery    True if the recipe is not yet added to the grocery list
     * @param ingredients       The ingredients observer to add the recipe ingredients to with a check value to show
     *                          if it is added to the grocery list.
     */
    void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery, ObservableArrayList<IngredientWithGroceryCheck> ingredients) throws ExecutionException, InterruptedException;

    /**
     * Delete All the recipe ingredients from the grocery
     * @param recipe    Recipe to delete from grocery
     * @param grocery   Grocery holding recipe and its ingredients to delete
     */
    void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery);
}
