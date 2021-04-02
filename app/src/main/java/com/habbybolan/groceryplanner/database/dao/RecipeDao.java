package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.relations.GroceryWithRecipes;
import com.habbybolan.groceryplanner.database.relations.RecipeWithIngredients;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsWithGroceryTuple;

import java.util.List;

/**
 * Sql commands dealing the Recipe and RecipeIngredientBridge queries.
 */
@Dao
public interface RecipeDao {

    // Recipe

    @Query("SELECT * FROM RecipeEntity")
    List<RecipeEntity> getAllRecipes();

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId")
    List<RecipeEntity> getAllRecipes(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = NULL")
    List<RecipeEntity> getAllUnCategorizedRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeEntity recipeEntity);

    @Delete
    void deleteRecipe(RecipeEntity recipeEntity);

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    RecipeEntity getRecipe(long recipeId);

    // Bridge table with Ingredients

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Delete
    void deleteFromRecipeIngredientBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId LIMIT 1")
    RecipeWithIngredients getIngredientsFromRecipe(long recipeId);

    @Query("SELECT * FROM RecipeIngredientBridge WHERE recipeId = :recipeId")
    List<RecipeIngredientBridge> getRecipeIngredient(long recipeId);

    @Query("SELECT recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "   quantity, quantity_type, food_type " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_type " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId)")
    List<RecipeIngredientsTuple> getRecipeIngredients(long recipeId);

    @Query("SELECT groceryId, recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "quantity, quantity_type, food_type " +
            "   FROM " +
            "   (SELECT ingredientId, ingredientName, price, price_per, price_type, " +
            "       quantity, quantity_type, food_type " +
            "       FROM " +
            "           (SELECT ingredientId, quantity, quantity_type " +
            "               FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "           JOIN " +
            "           (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "               FROM IngredientEntity) " +
            "           USING (ingredientId))" +
            "LEFT JOIN" +
            "   (SElECT groceryId, recipeId, ingredientId " +
            "       FROM GroceryRecipeIngredientEntity" +
            "       WHERE recipeId = :recipeId AND groceryId = :groceryId) " +
            "USING (ingredientId)")
    List<RecipeIngredientsWithGroceryTuple> getRecipeIngredientsInGrocery(long recipeId, long groceryId);


    // Recipe Category

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :id")
    List<RecipeEntity> getRecipesInCategory(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);


    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId=:recipeId")
    void deleteRecipeFromRecipeIngredientBridge(long recipeId);

    @Delete
    void deleteRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);

    @Update
    void updateRecipes(RecipeEntity recipeEntity);

    @Query("SELECT * FROM RecipeCategoryEntity")
    List<RecipeCategoryEntity> getAllRecipeCategories();

    @Query("SELECT * FROM RecipeCategoryEntity WHERE recipeCategoryId = :recipeCategoryId")
    RecipeCategoryEntity getRecipeCategory(long recipeCategoryId);

    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT RIB.ingredientId FROM RecipeIngredientBridge RIB " +
            "WHERE RIB.recipeId == :recipeId)")
    List<IngredientEntity> getIngredientsNotInRecipe(long recipeId);

    @Delete
    void deleteRecipeFromGroceryInGroceryRecipeBridge(GroceryRecipeBridge groceryRecipeBridge);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId = :recipeId")
    void deleteRecipeFromGroceryRecipeBridge(long recipeId);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId")
    void deleteRecipeFromGroceryRecipeIngredient(long recipeId);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND groceryId=:groceryId")
    void deleteRecipeGroceryFromGroceryRecipeIngredient(long groceryId, long recipeId);

    @Query("SELECT * FROM GroceryEntity " +
            "WHERE groceryId NOT IN " +
            "(SELECT GRB.groceryId FROM GroceryRecipeBridge GRB " +
            "WHERE GRB.recipeId == :recipeId)")
    List<GroceryEntity> getGroceriesNotHoldingRecipe(long recipeId);

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId LIMIT 1")
    GroceryWithRecipes getGroceriesHoldingRecipe(long recipeId);

    @Query("SELECT * FROM GroceryRecipeBridge WHERE recipeId = :recipeId")
    List<GroceryRecipeBridge> getRecipeAmountsInsideGrocery(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeIntoGrocery(GroceryRecipeBridge groceryRecipeBridge);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND ingredientId=:ingredientId")
    void deleteRecipeIngredientFromGroceryRecipeIngredient(long recipeId, long ingredientId);


}
