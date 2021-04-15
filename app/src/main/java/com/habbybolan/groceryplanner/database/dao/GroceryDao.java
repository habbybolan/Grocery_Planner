package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.relations.GroceryWithIngredients;
import com.habbybolan.groceryplanner.models.databasetuples.GroceryIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.GroceryRecipeIngredientTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientInGroceryTuple;

import java.util.List;

/**
 * Sql commands dealing with Grocery and groceryIngredientBridge queries.
 */
@Dao
public interface GroceryDao {

    // Grocery table accesses

    @Transaction
    @Query("SELECT * FROM GroceryEntity WHERE groceryId = :id")
    List<GroceryWithIngredients> getGroceryWithIngredients(long id);

    @Query("SELECT * FROM GroceryEntity")
    List<GroceryEntity> getAllGroceries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrocery(GroceryEntity groceryEntity);

    @Delete
    void deleteGrocery(GroceryEntity groceryEntity);

    @Query("DELETE FROM GroceryEntity WHERE groceryId IN (:groceryIds)")
    void deleteGroceries(List<Long> groceryIds);

    @Query("DELETE FROM GroceryIngredientBridge WHERE groceryId IN (:groceryIds)")
    void deleteGroceriesFromBridge(List<Long> groceryIds);

    // Bridge table access
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(GroceryIngredientBridge groceryIngredientBridge);

    @Delete
    void deleteFromGroceryIngredientBridge(GroceryIngredientBridge groceryIngredientBridge);

    @Delete
    void deleteFromGroceryIngredientEntity(GroceryIngredientEntity groceryIngredientEntity);

    @Query("DELETE FROM GroceryRecipeBridge WHERE groceryId IN (:groceryIds)")
    void deleteGroceriesFromGroceryRecipeBridge(List<Long> groceryIds);

    @Query("DELETE FROM GroceryIngredientEntity WHERE groceryId IN (:groceryIds)")
    void deleteGroceriesFromGroceryIngredientEntity(List<Long> groceryIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE groceryId IN (:groceryIds)")
    void deleteGroceriesFromGroceryRecipeIngredientEntity(List<Long> groceryIds);

    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT GIE.ingredientId FROM GroceryIngredientEntity GIE " +
            "WHERE GIE.groceryId == :groceryId) ")
    List<IngredientEntity> getIngredientsNotInGrocery(long groceryId);

    @Query("SELECT recipeId, ingredientId, groceryId, ingredientId, ingredientName, price, price_per, price_type, is_checked, food_type, " +
            "GR.quantity AS recipeQuantity, GR.quantity_type AS recipeQuantityType, GI.quantity AS groceryQuantity, GI.quantity_type AS groceryQuantityType, recipeName, amount " +
            "FROM " +
            "   (SELECT groceryId, recipeId, amount, quantity, quantity_type, r.name AS recipeName " +
            "       FROM " +
            "       (SELECT groceryId, GRB.recipeId, amount, ingredientId, quantity, quantity_type " +
            "           FROM GroceryRecipeIngredientEntity " +
            "           JOIN GroceryRecipeBridge GRB" +
            "           USING (groceryId)) " +
            "       JOIN RecipeEntity r" +
            "       USING (recipeId)) AS GR " +
            "JOIN " +
            "   (SELECT groceryId, ingredientId, ingredientName, price, price_per, price_type, food_type, is_checked, quantity, quantity_type " +
            "       FROM " +
            "       (SELECT groceryId, i.ingredientId, i.ingredientName, price, i.price_per, i.price_type, i.food_type, " +
            "           GIE.quantity, GIE.quantity_type" +
            "           FROM IngredientEntity i " +
            "           JOIN GroceryIngredientEntity GIE " +
            "           USING (ingredientId) " +
            "           WHERE GIE.groceryId = :groceryId)" +
            "       JOIN " +
            "           (SELECT groceryId, is_checked FROM GroceryIngredientBridge GIB) " +
            "       USING (groceryId)) AS GI " +
            "USING (groceryId)")
    List<GroceryRecipeIngredientTuple> getRecipeGroceryIngredients(long groceryId);

    @Query("SELECT groceryId, ingredientId, ingredientName, price, price_per, price_type, food_type, is_checked, quantity, quantity_type " +
            "       FROM " +
            "       (SELECT groceryId, i.ingredientId, i.ingredientName, price, i.price_per, i.price_type, i.food_type, " +
            "           GIE.quantity, GIE.quantity_type" +
            "           FROM IngredientEntity i " +
            "           JOIN GroceryIngredientEntity GIE " +
            "           USING (ingredientId) " +
            "           WHERE GIE.groceryId = :groceryId)" +
            "       JOIN " +
            "           (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge GIB WHERE groceryId = :groceryId) " +
            "       USING (groceryId, ingredientId) ")
    List<GroceryIngredientsTuple> getGroceryIngredients(long groceryId);

    @Query("SELECT recipeId, groceryId, recipeName, ingredientId, ingredientName, amount, price, " +
            " price_per, price_type, food_type, quantity, quantity_type, is_checked, amount " +
            "       FROM " +
                        /* Get ingredient values */
            "           (SELECT ingredientId, ingredientName, price, price_per, price_type, food_type FROM IngredientEntity) " +
            "           JOIN" +
                        /* Get is_checked */
            "           (SELECT groceryId, ingredientId, recipeId, recipeName, amount, ingredientId, quantity, quantity_type, is_checked " +
            "               FROM " +
            "               (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge)" +
            "               JOIN " +
                            /* Get recipe name */
            "               (SELECT groceryId, recipeId, name AS recipeName, amount, ingredientId, quantity, quantity_type " +
            "                   FROM " +
                                /* get amount and quantities of recipe ingredients */
            "                   (SELECT groceryId, recipeId, amount, ingredientId, quantity, quantity_type " +
            "                       FROM (SELECT groceryId, recipeId, ingredientId, quantity, quantity_type FROM GroceryRecipeIngredientEntity WHERE groceryId=:groceryId)" +
            "                       JOIN (SELECT amount, recipeId FROM GroceryRecipeBridge WHERE groceryId=:groceryId)" +
            "                       USING (recipeId)) " +
            "                   JOIN (SELECT recipeId, name FROM recipeentity)" +
            "                   USING (recipeId))" +
            "               USING (groceryId, ingredientId))" +
            "           USING (ingredientId)")
    List<RecipeIngredientInGroceryTuple> getRecipeIngredientsInGrocery(long groceryId);

    @Query("SELECT COUNT(ingredientId) FROM GroceryIngredientBridge WHERE groceryId = :groceryId AND ingredientId = :ingredientId ")
    int getCountIngredientInGroceryBridge(long groceryId, long ingredientId);

    @Query("DELETE FROM GroceryIngredientEntity WHERE groceryId=:groceryId AND ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromGroceryIngredient(long groceryId, List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE groceryId=:groceryId AND ingredientId IN (:ingredientIds)")
    void deleteGroceryIngredientsFromGroceryRecipeIngredientEntity(long groceryId, List<Long> ingredientIds);

    @Query("DELETE FROM GroceryIngredientBridge AS GIB " +
            "    WHERE NOT EXISTS " +
            "           (SELECT * " +
            "           FROM " +
            "               (SELECT * FROM " +
            "                   (SELECT groceryId, ingredientId FROM GroceryRecipeIngredientEntity) " +
            "                   UNION ALL SELECT groceryId, ingredientId FROM GroceryIngredientEntity) GI " +
            "               WHERE GI.ingredientId = GIB.ingredientId AND GI.groceryId = GIB.groceryId)" )
    void deleteIngredientsNotInGroceryAnymore();

    @Query("DELETE FROM GroceryRecipeBridge AS GRB " +
            "   WHERE GRB.groceryId = :groceryId" +
            "   AND GRB.recipeId NOT IN (SELECT recipeId FROM GroceryRecipeIngredientEntity WHERE groceryId = :groceryId)")
    void deleteRecipeIfIngredientsNotInGroceryAnymore(long groceryId);

    @Update
    void updateGroceryIngredientChecked(GroceryIngredientBridge bridge);

    @Delete
    void deleteGroceryRecipe(GroceryRecipeBridge groceryRecipeBridge);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroceryRecipeIngredient(GroceryRecipeIngredientEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroceryIngredient(GroceryIngredientEntity entity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIngredientIntoGrocery(GroceryIngredientBridge entity);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE ingredientId = :ingredientId AND recipeId = :recipeId AND groceryId = :groceryId")
    void deleteRecipeIngredientFromGrocery(long ingredientId, long recipeId, long groceryId);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE ingredientId = :recipeId AND groceryId = :groceryId")
    void deleteRecipeFromGroceryRecipeIngredient(long recipeId, long groceryId);



}
