package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DatabaseAccess {

    void deleteGrocery(Long groceryId);
    void deleteGroceries(List<Long> groceryIds);
    void addGrocery(Grocery grocery);
    void fetchGroceries(DbCallback<Grocery> callback, SortType sortType) throws ExecutionException, InterruptedException;

    /**
     * Fetch the groceries that contain the recipe, and store the groceries inside groceries
     * @param offlineRecipe        Recipe inside the groceries
     * @param callback      Callback for updating the GroceryRecipes fetched
     */
    void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Fetch the groceries that does not contain the recipe, and store the groceries inside groceries
     * @param offlineRecipe        Recipe not inside the groceries
     * @param callback      Callback for updating the Groceries fetched
     */
    void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;

    /**
     * Adds / updates the recipe to the grocery list.
     * @param offlineRecipe            Recipe to add to grocery
     * @param grocery           grocery to hold the recipe
     * @param amount            The number of times to add the Recipe to the grocery
     * @param recipeIngredients recipe ingredients to with a check to add or remove from grocery list
     */
    void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients);

    /**
     * Remove a recipe ingredient relationship with the grocery
     * @param groceryId     id of grocery holding the recipe ingredient
     * @param recipeId      id of recipe holding the ingredient
     * @param ingredientId  id of the ingredient
     */
    void deleteRecipeIngredientFromGrocery(long groceryId, long recipeId, long ingredientId);

    /**
     * Remove the direct relationship between an ingredient and a grocery list
     * @param groceryId     id of the grocery
     * @param ingredientId  id of the ingredient
     */
    void deleteDirectIngredientFromGrocery(long groceryId, long ingredientId);

    /**
     * Delete the Grocery Recipe relationship in GroceryRecipeBridge
     * @param offlineRecipe    Recipe to be removed from relationship
     * @param grocery   Grocery to be removed from relationship
     */
    void deleteGroceryRecipeBridge(OfflineRecipe offlineRecipe, Grocery grocery);

    void deleteRecipe(Long recipeId);
    void deleteRecipes(List<Long> recipeIds);
    void addRecipe(OfflineRecipe offlineRecipe, Timestamp dateCreated);
    void fetchRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    List<OfflineRecipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException;
    void updateRecipes(ArrayList<OfflineRecipe> offlineRecipes);
    void updateRecipe(OfflineRecipe offlineRecipe);
    void fetchRecipe(ObservableField<OfflineRecipe> recipeObserver, long recipeId) throws ExecutionException, InterruptedException;

    void addRecipeTag(long recipeId, String title);
    void addRecipeTags(long recipeId, List<String> titles);
    void fetchRecipeTags(long recipeId, DbCallback<RecipeTag> recipeTags) throws ExecutionException, InterruptedException;
    void deleteRecipeTag(long recipeId, long tagId);

    void deleteRecipeCategory(long categoryId);
    void deleteRecipeCategories(List<Long> categoryIds);
    void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException;
    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException;
    void addRecipeCategory(RecipeCategory recipeCategory);

    /**
     * Insert an ingredient into a grocery list
     * @param groceryId      id of Grocery list to insert the ingredient into
     * @param ingredients    Ingredients to insert
     */
    void insertIngredientsIntoGrocery(long groceryId, List<Ingredient> ingredients);
    /**
     * Insert an ingredient into a recipe list
     * @param recipeId       Id of Recipe list to insert the ingredient into
     * @param ingredients    Ingredients to insert
     */
    void insertIngredientsIntoRecipe(long recipeId, List<Ingredient> ingredients);
    void deleteIngredient(long ingredientId);
    void deleteIngredients(List<Long> ingredientIds);
    /**
     * Delete a list of ingredients from the grocery list
     * @param groceryId       The grocery id to delete the ingredients from
     * @param ingredientIds   The list of ingredient ids to delete from the grocery
     */
    void deleteIngredientsFromGrocery(long groceryId, List<Long> ingredientIds);
    /**
     * Delete a list of ingredients from the recipe list
     * @param recipeId        The recipe id to delete the ingredients from
     * @param ingredientIds   The list of ingredient ids to delete from the recipe
     */
    void deleteIngredientsFromRecipe(long recipeId, List<Long> ingredientIds);
    void fetchIngredientsFromRecipe(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

    void addIngredient(Ingredient ingredient);

    /**
     * Fetch all ingredients created.
     * @param callback   callback for updating the ingredients fetched.
     */
    void fetchIngredients(DbCallback<Ingredient> callback, SortType sortType) throws ExecutionException, InterruptedException;

    void deleteRecipeFromGrocery(Grocery grocery, OfflineRecipe offlineRecipe);

    /**
     * Called for querying a recipe's ingredients where the recipe is inside the grocery. Retrieves all ingredients the recipe
     * with info if the ingredient was added to grocery.
     * @param grocery               Grocery list holding the recipe
     * @param offlineRecipe                Recipe holding the ingredients to query
     * @param callback              callback for updating the list of recipe ingredients fetched
     */
    void fetchRecipeIngredientsInGrocery(Grocery grocery, OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

    /**
     * Called for querying a recipe that is known is not inside a grocery list. Retrieves all of its ingredients inside the recipe.
     * @param offlineRecipe                Recipe to query for ingredients
     * @param callback              callback for updating the list of recipe ingredients fetched
     */
    void fetchRecipeIngredientsNotInGrocery(OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException;

    /**
     * Get all the Ingredients that are not part of the recipe
     * @param offlineRecipe                Recipe that is not holding the ingredients to fetch
     * @param callback              callback for updating ingredients not in recipe
     */
    void fetchIngredientsNotInRecipe(OfflineRecipe offlineRecipe, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
    /**
     * Get all the Ingredients that are not part of the grocery
     * @param grocery     grocery that is not holding the ingredients to fetch
     * @param callback    callback for updating ingredients not in grocery
     */
    void fetchIngredientsNotInGrocery(Grocery grocery, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

    /**
     * Fetch the Ingredients inside a Grocery, both directly added Ingredients, or through the recipes
     * added to the Grocery list
     * @param grocery     The grocery list holding the ingredients
     * @param callback    callback for updating the Ingredient inside a grocery list
     */
    void fetchGroceryIngredients(Grocery grocery, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException;

    /**
     * Update the ingredients bridges with new isChecked
     * @param grocery               Grocery holding the ingredient to update
     * @param groceryIngredient     holds the check value to change it to
     */
    void updateGroceryIngredientChecked(Grocery grocery, GroceryIngredient groceryIngredient);

    /**
     * Search for any grocery with the grocerySearch name.
     * @param grocerySearch grocery name to search
     * @param callback      callback to update the Groceries retrieved
     */
    void searchGroceries(String grocerySearch, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any grocery ingredient with the ingredientSearch name.
     * @param groceryId         id of the grocery
     * @param ingredientSearch  grocery ingredient name to search
     * @param callback          callback to update the Grocery Ingredients retrieved
     */
    void searchGroceryIngredients(long groceryId, String ingredientSearch, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any recipe with the recipeSearch name.
     * @param recipeSearch  recipe name to search
     * @param callback    callback to update the Recipes retrieved
     */
    void searchRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any recipe inside the category with the recipeSearch name.
     * @param categoryId        id of the recipe category
     * @param recipeSearch      recipe name to search
     *  @param callback         callback to update the Recipes in a category retrieved
     */
    void searchRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any recipe category with the recipeSearch name.
     * @param categorySearch    category name to search
     * @param callback          callback to update the categories retrieved
     */
    void searchRecipeCategories(String categorySearch, DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any recipe ingredient with the ingredientSearch name.
     * @param recipeId          id of the recipe
     * @param ingredientSearch  recipe ingredient name to search
     *  @param callback         callback to update the Recipe Ingredients retrieved
     */
    void searchRecipeIngredients(long recipeId, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for any ingredient with the ingredientSearch name.
     * @param ingredientSearch  ingredient name to search
     *  @param callback         callback to update the Ingredients retrieved
     */
    void searchIngredients(String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
}
