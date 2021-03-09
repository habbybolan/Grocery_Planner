package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.relations.RecipeWithIngredients;

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

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId = :recipeId")
    void deleteRecipeFromBridge(long recipeId);

    // Bridge table with Ingredients

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Delete
    void deleteFromBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Transaction
    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId LIMIT 1")
    RecipeWithIngredients getIngredientsFromRecipe(long recipeId);

    @Query("SELECT * FROM RecipeIngredientBridge WHERE recipeId = :recipeId")
    List<RecipeIngredientBridge> getRecipeIngredient(long recipeId);

    @Query("SELECT * FROM RecipeIngredientBridge WHERE recipeId = :recipeId AND ingredientName = :ingredientName")
    List<RecipeIngredientBridge> getRecipeIngredient(long recipeId, long ingredientName);

    // Recipe Category

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :id")
    List<RecipeEntity> getRecipesInCategory(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);

    @Delete
    void deleteRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);

    @Update
    void updateRecipes(RecipeEntity recipeEntity);

    @Query("SELECT * FROM RecipeCategoryEntity")
    List<RecipeCategoryEntity> getAllRecipeCategories();
}
