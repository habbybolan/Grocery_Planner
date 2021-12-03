package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
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

    // adds a new recipe
    void insertMyRecipe(OfflineRecipe offlineRecipe, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Inserts an entire MyRecipe, including ingredients, tags and nutrition
     * @param myRecipe  Full recipe to insert
     * @param callback  Called after insert completed, sending back identifying part of the inserted recipe
     */
    void insertFullMyRecipe(MyRecipe myRecipe, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    void fetchMyRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    void fetchLikedRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    List<OfflineRecipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException;
    void updateRecipes(ArrayList<OfflineRecipe> offlineRecipes);
    void updateRecipe(OfflineRecipe recipe);
    void fetchFullMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException;
    void fetchFullLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException;

    void addRecipeTag(long recipeId, String title);
    void addRecipeTags(long recipeId, List<String> titles);
    void fetchRecipeTags(long recipeId, DbCallback<RecipeTag> recipeTags) throws ExecutionException, InterruptedException;
    void deleteRecipeTagFromBridge(long recipeId, RecipeTag recipeTag);

    void deleteRecipeCategory(long categoryId);
    void deleteRecipeCategories(List<Long> categoryIds);
    void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException;
    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException;
    void addRecipeCategory(RecipeCategory recipeCategory);

    void deleteNutrition(long recipeId, long nutritionId);
    void addNutrition(long recipeId, Nutrition nutrition);


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

    void addIngredient(Ingredient ingredient, DbSingleCallback<Ingredient> callback);

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
     * Search for a MyRecipe with the recipeSearch name.
     * @param recipeSearch  recipe name to search
     * @param callback    callback to update the Recipes retrieved
     */
    void searchMyRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for a LikedRecipe with the recipeSearch name.
     * @param recipeSearch  recipe name to search
     * @param callback    callback to update the Recipes retrieved
     */
    void searchLikedRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for a MyRecipe inside the category with the recipeSearch name.
     * @param categoryId        id of the recipe category
     * @param recipeSearch      recipe name to search
     *  @param callback         callback to update the Recipes in a category retrieved
     */
    void searchMyRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

    /**
     * Search for a LikedRecipe inside the category with the recipeSearch name.
     * @param categoryId        id of the recipe category
     * @param recipeSearch      recipe name to search
     *  @param callback         callback to update the Recipes in a category retrieved
     */
    void searchLikedRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;

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

    /**
     * Sync the my recipe data retrieved from the web service by updating offline
     * @param accessLevelId access level to recipe
     * @param recipeEntity  Recipe data to update
     */
    void syncMyRecipeUpdate(RecipeEntity recipeEntity, long accessLevelId);

    /**
     * Sync the liked recipe data retrieved from the web service by updating offline
     * @param recipeEntity  Recipe data to update
     */
    void syncLikedRecipeUpdate(RecipeEntity recipeEntity);

    /**
     * Sync my recipe data retrieved from the web service by inserting offline
     * @param recipeEntity  My Recipe data to insert
     * @param accessLevelId access level to recipe
     * @return              id of newly inserted recipe
     */
    long syncMyRecipeInsert(RecipeEntity recipeEntity, long accessLevelId);

    /**
     * Sync liked recipe data retrieved from the web service by inserting offline
     * @param recipeEntity  liked Recipe data to insert
     * @return              id of newly inserted recipe
     */
    long syncLikedRecipeInsert(RecipeEntity recipeEntity);

    /**
     * Update the recipe's date synchronized only.
     * Called after updating or inserting in online database.
     * @param recipeId          offline id of recipe
     * @param dateSync          Date sent from web service on time it was synced
     * @param onlineRecipeId    Online id of recipe if it was inserted online, null otherwise
     */
    void syncRecipeUpdateSynchronized(long recipeId, Long onlineRecipeId, Timestamp dateSync);

    /**
     * Sync the Ingredient data retrieved from the web service by updating/Inserting based on the identifier.
     * @param ingredientEntity
     * @param identifier    Identifies if the recipe will be updated or inserted
     */
    void syncIngredientUpdateInsert(IngredientEntity ingredientEntity, long recipeId, long ingredientId,
                                    float quantity, Long measurementId, Timestamp dateSynchronized, boolean isDeleted, long foodType, SyncJSON.OnlineUpdateIdentifier identifier);

    /**
     * Update the ingredients date synchronized only.
     * Called after updating or inserting in online database.
     * @param recipeId              Offline id of recipe the ingredient is in
     * @param dateSync              Date sent from web service on time it was synced
     * @param ingredientId          offline id of ingredient
     * @param onlineIngredientId    Online id of ingredient if it was inserted, null otherwise
     */
    void syncIngredientUpdateSynchronized(long recipeId, long ingredientId, Long onlineIngredientId, Timestamp dateSync);

    /**
     * Sync the recipe tag data retrieved from the web service by updating/Inserting based on the identifier.
     * @param recipeTagEntity
     * @param identifier    Identifies if the tag will be updated or inserted
     */
    void syncRecipeTagUpdateInsert(RecipeTagEntity recipeTagEntity, long recipeId, long tagId, Timestamp dateSynchronized, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier);

    /**
     * Update the tag's date synchronized only.
     * Called after updating or inserting in online database.
     * @param recipeId      Offline id of recipe the tag is in
     * @param dateSync      Date sent from web service on time it was synced
     * @param tagId         Offline id of tag
     * @param onlineTagId   Online id of tag if it was inserted, null otherwise
     */
    void syncRecipeTagUpdateSynchronized(long recipeId, long tagId, Long onlineTagId, Timestamp dateSync);

    /**
     * Sync the nutrition data retrieved from the web service by updating/Inserting based on the identifier.
     * @param identifier    Identifies if the nutrition will be updated or inserted
     */
    void syncNutritionUpdateInsert(long recipeId, long nutritionId, int amount, Long measurementId, Timestamp dateSynchronized, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier);


    /**
     * Update the nutrition's date synchronized only.
     * Called after updating or inserting in online database.
     * @param recipeId      Offline id of recipe the nutrition is in
     * @param dateSync      Date sent from web service on time it was synced
     * @param nutritionId   Offline id of nutrition
     */
    void syncNutritionUpdateSynchronized(long recipeId, long nutritionId, Timestamp dateSync);
}
