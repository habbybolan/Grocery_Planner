package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeGroceriesTuple;
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

    @Query("SELECT * FROM RecipeEntity ORDER BY name ASC")
    List<RecipeEntity> getAllRecipesSortAlphabeticalAsc();

    @Query("SELECT * FROM RecipeEntity ORDER BY name DESC")
    List<RecipeEntity> getAllRecipesSortAlphabeticalDesc();

    @Query("SELECT * FROM RecipeEntity ORDER BY date_created DESC")
    List<RecipeEntity> getAllRecipesSortDateDesc();

    @Query("SELECT * FROM RecipeEntity ORDER BY date_created ASC")
    List<RecipeEntity> getAllRecipesSortDateAsc();

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId")
    List<RecipeEntity> getAllRecipes(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY name ASC")
    List<RecipeEntity> getAllRecipesSortAlphabeticalAsc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY name DESC")
    List<RecipeEntity> getAllRecipesSortAlphabeticalDesc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY date_created DESC")
    List<RecipeEntity> getAllRecipesSortDateDesc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY date_created ASC")
    List<RecipeEntity> getAllRecipesSortDateAsc(long categoryId);

    @Query("SELECT * FROM RecipeCategoryEntity")
    List<RecipeCategoryEntity> getAllRecipeCategories();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name ASC")
    List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalAsc();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name DESC")
    List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalDesc();

    @Query("SELECT * FROM RecipeCategoryEntity WHERE recipeCategoryId = :recipeCategoryId")
    RecipeCategoryEntity getRecipeCategory(long recipeCategoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = NULL")
    List<RecipeEntity> getAllUnCategorizedRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(RecipeEntity recipeEntity);

    @Delete
    void deleteRecipe(RecipeEntity recipeEntity);

    @Query("DELETE FROM RecipeEntity WHERE recipeId IN (:recipeIds)")
    void deleteRecipes(List<Long> recipeIds);

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    RecipeEntity getRecipe(long recipeId);

    // Bridge table with Ingredients

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Delete
    void deleteFromRecipeIngredientBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromRecipeIngredientBridge(long recipeId, List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    void deleteRecipeIngredientsFromGroceryRecipeIngredientEntity(long recipeId, List<Long> ingredientIds);

    @Query("SELECT * FROM RecipeIngredientBridge WHERE recipeId = :recipeId")
    List<RecipeIngredientBridge> getRecipeIngredient(long recipeId);

    @Query("SELECT recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "   quantity, quantityMeasId, food_type " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId)")
    List<RecipeIngredientsTuple> getRecipeIngredients(long recipeId);

    @Query("SELECT recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "   quantity, quantityMeasId, food_type " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) ORDER BY ingredientName ASC")
    List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalAsc(long recipeId);

    @Query("SELECT recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "   quantity, quantityMeasId, food_type " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) ORDER BY ingredientName DESC")
    List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalDesc(long recipeId);

    @Query("SELECT groceryId, recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "quantity, quantityMeasId, food_type " +
            "   FROM " +
            "   (SELECT ingredientId, ingredientName, price, price_per, price_type, " +
            "       quantity, quantityMeasId, food_type " +
            "       FROM " +
            "           (SELECT ingredientId, quantity, quantity_meas_id AS quantityMeasId " +
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


    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId IN (:recipeIds)")
    void deleteRecipesFromRecipeIngredientBridge(List<Long> recipeIds);

    @Query("DELETE FROM RecipeCategoryEntity WHERE recipeCategoryId IN (:categoryIds)")
    void deleteRecipeCategories(List<Long> categoryIds);

    @Update
    void updateRecipes(RecipeEntity recipeEntity);

    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT RIB.ingredientId FROM RecipeIngredientBridge RIB " +
            "WHERE RIB.recipeId == :recipeId)")
    List<IngredientEntity> getIngredientsNotInRecipe(long recipeId);

    @Delete
    void deleteRecipeFromGroceryInGroceryRecipeBridge(GroceryRecipeBridge groceryRecipeBridge);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId IN (:recipeIds)")
    void deleteRecipesFromGroceryRecipeBridge(List<Long> recipeIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId IN (:recipeIds)")
    void deleteRecipeFromGroceryRecipeIngredient(List<Long> recipeIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND groceryId=:groceryId")
    void deleteRecipeGroceryFromGroceryRecipeIngredient(long groceryId, long recipeId);

    @Query("SELECT * FROM GroceryEntity " +
            "WHERE groceryId NOT IN " +
            "(SELECT GRB.groceryId FROM GroceryRecipeBridge GRB " +
            "WHERE GRB.recipeId == :recipeId)")
    List<GroceryEntity> getGroceriesNotHoldingRecipe(long recipeId);

    @Query("SELECT * FROM GroceryRecipeBridge WHERE recipeId = :recipeId")
    List<GroceryRecipeBridge> getRecipeAmountsInsideGrocery(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeIntoGrocery(GroceryRecipeBridge groceryRecipeBridge);

    @Query("SELECT groceryId, groceryName, amount " +
            "   FROM" +
            "   (SELECT groceryId, amount FROM GroceryRecipeBridge WHERE recipeId=:recipeId) " +
            "   JOIN" +
            "   (SELECT groceryId, name AS groceryName FROM GroceryEntity) " +
            "   USING (groceryId)")
    List<RecipeGroceriesTuple> getGroceriesHoldingRecipe(long recipeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTag(RecipeTagEntity tag);

    @Insert
    void insertRecipeTagBridge(RecipeTagBridge bridge);

    @Query("SELECT tagId, title FROM " +
            "   (SELECT tagId FROM RecipeTagBridge WHERE recipeId=:recipeId)" +
            "   JOIN" +
            "   (SELECT tagId, title FROM RecipeTagEntity)" +
            "   USING  (tagId)")
    List<RecipeTagEntity> getRecipeTags(long recipeId);

    @Delete
    void deleteRecipeTagBridge(RecipeTagBridge bridge);

    @Query("SELECT tagId, title FROM RecipeTagEntity WHERE title=:title")
    RecipeTagEntity getRecipeTag(String title);

    @Query("SELECT * FROM RecipeEntity WHERE name like :recipeSearch")
    List<RecipeEntity> searchRecipes(String recipeSearch);

    @Query("SELECT * FROM RecipeCategoryEntity WHERE name like :categorySearch")
    List<RecipeCategoryEntity> searchRecipeCategories(String categorySearch);

    @Query("SELECT * FROM RecipeEntity WHERE name like :recipeSearch AND recipe_category_id = :categoryId")
    List<RecipeEntity> searchRecipesInCategory(long categoryId, String recipeSearch);

    @Query("SELECT recipeId, ingredientId, ingredientName, price, price_per, price_type, " +
            "   quantity, quantityMeasId, food_type " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type " +
            "           FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch) " +
            "       USING (ingredientId)")
    List<RecipeIngredientsTuple> searchRecipeIngredients(long recipeId, String ingredientSearch);
}
