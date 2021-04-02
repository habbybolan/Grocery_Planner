package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.details.recipe.recipeoverview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.primarymodels.Step;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DatabaseAccess {

    void deleteGrocery(Grocery grocery);
    void deleteGroceries(List<Grocery> groceries);
    void addGrocery(Grocery grocery);
    void fetchGroceries(ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException;

    /**
     * Fetch the groceries that contain the recipe, and store the groceries inside groceries
     * @param recipe        Recipe inside the groceries
     * @param groceries     The groceries holding the recipe and the amount of the recipe it holds
     */
    void fetchGroceriesHoldingRecipe(Recipe recipe, ObservableArrayList<GroceryRecipe> groceries) throws ExecutionException, InterruptedException;

    /**
     * Fetch the groceries that does not contain the recipe, and store the groceries inside groceries
     * @param recipe        Recipe not inside the groceries
     * @param groceries     The groceries not holding the recipe
     */
    void fetchGroceriesNotHoldingRecipe(Recipe recipe, ObservableArrayList<Grocery> groceries) throws ExecutionException, InterruptedException;

    /**
     * Adds the recipe to the grocery list.
     * @param recipe            Recipe to add to grocery
     * @param grocery           grocery to hold the recipe
     * @param amount            The number of times to add the Recipe to the grocery
     * @param recipeIngredients recipe ingredients to with a check to add or remove from grocery list
     */
    void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);

    /**
     * Delete the Grocery Recipe relationship in GroceryRecipeBridge
     * @param recipe    Recipe to be removed from relationship
     * @param grocery   Grocery to be removed from relationship
     */
    void deleteGroceryRecipeBridge(Recipe recipe, Grocery grocery);

    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe);
    void fetchRecipes(Long recipeCategoryId, ObservableArrayList<Recipe> recipesObserver) throws ExecutionException, InterruptedException;
    List<Recipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException;
    void updateRecipes(ArrayList<Recipe> recipes);
    void fetchRecipe(ObservableField<Recipe> recipeObserver, long recipeId) throws ExecutionException, InterruptedException;

    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    void fetchRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException;
    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException;
    void addRecipeCategory(RecipeCategory recipeCategory);

    List<Step> fetchStepsFromRecipe(long recipeId) throws ExecutionException, InterruptedException;
    void addStep(Step step);

    void addIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void addIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredientHolderRelationship(IngredientHolder ingredientHolder, Ingredient ingredient);
    void fetchIngredientsFromRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;

    void deleteRecipeFromGrocery(Grocery grocery, Recipe recipe);
    //void fetchIngredientsFromRecipeWithCheck(Recipe recipe, Grocery grocery, ObservableArrayList<IngredientWithCheck> ingredientObserver) throws ExecutionException, InterruptedException;

    /**
     * Called for querying a recipe's ingredients where the recipe is inside the grocery. Retrieves all ingredients the recipe
     * with info if the ingredient was added to grocery.
     * @param grocery               Grocery list holding the recipe
     * @param recipe                Recipe holding the ingredients to query
     * @param ingredientObserver    Observer for the queries recipe ingredients
     */
    void fetchRecipeIngredientsInGrocery(Grocery grocery, Recipe recipe, ObservableArrayList<IngredientWithGroceryCheck> ingredientObserver) throws ExecutionException, InterruptedException;

    /**
     * Called for querying a recipe that is known is not inside a grocery list. Retrieves all of its ingredients inside the recipe.
     * @param recipe                Recipe to query for ingredients
     * @param ingredientObserver    Observer for the queried ingredients
     */
    void fetchRecipeIngredientsNotInGrocery(Recipe recipe, ObservableArrayList<IngredientWithGroceryCheck> ingredientObserver) throws ExecutionException, InterruptedException;

    /**
     * Get all the Ingredients that are not part of the recipe
     * @param recipe                Recipe that is not holding the ingredients to fetch
     * @param ingredientsObserver   Observer for list of ingredients not in recipe
     */
    void fetchIngredientsNotInRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;
    /**
     * Get all the Ingredients that are not part of the grocery
     * @param grocery                grocery that is not holding the ingredients to fetch
     * @param ingredientsObserver    Observer for list of ingredients not in grocery
     */
    void fetchIngredientsNotInGrocery(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Ingredients inside a Grocery, both directly added Ingredients, or through the recipes
     * added to the Grocery list
     * @param grocery                       The grocery list holding the ingredients
     * @param groceryIngredientsObserver    Holds the Ingredient that displays how they are associated
     *                                      with the grocery list
     */
    void fetchGroceryIngredients(Grocery grocery, ObservableArrayList<GroceryIngredient> groceryIngredientsObserver) throws ExecutionException, InterruptedException;

    /**
     * Update the ingredients bridges with new isChecked
     * @param grocery               Grocery holding the ingredient to update
     * @param groceryIngredient     holds the check value to change it to
     */
    void updateGroceryIngredientChecked(Grocery grocery, GroceryIngredient groceryIngredient);
}
