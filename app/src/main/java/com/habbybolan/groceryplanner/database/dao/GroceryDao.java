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
import com.habbybolan.groceryplanner.models.databasetuples.GroceryIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientInGroceryTuple;

import java.util.List;

/**
 * Sql commands dealing with Grocery and groceryIngredientBridge queries.
 */
@Dao
public interface GroceryDao {

    // Grocery table accesses

    // not used
    /*@Transaction
    @Query("SELECT * FROM GroceryEntity WHERE groceryId = :id")
    List<GroceryWithIngredients> getGroceryWithIngredients(long id);*/

    @Query("SELECT * FROM GroceryEntity")
    List<GroceryEntity> getAllGroceries();

    @Query("SELECT * FROM GroceryEntity ORDER BY name ASC")
    List<GroceryEntity> getAllGroceriesSortAlphabeticalAsc();

    @Query("SELECT * FROM GroceryEntity ORDER BY name DESC")
    List<GroceryEntity> getAllGroceriesSortAlphabeticalDesc();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrocery(GroceryEntity groceryEntity);

    @Delete
    void deleteGrocery(GroceryEntity groceryEntity);

    @Query("DELETE FROM GroceryEntity WHERE groceryId IN (:groceryIds)")
    void deleteGroceries(List<Long> groceryIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(GroceryIngredientBridge groceryIngredientBridge);

    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT GIE.ingredientId FROM GroceryIngredientEntity GIE " +
            "WHERE GIE.groceryId == :groceryId) " +
            "AND ingredientName LIKE :search ")
            // TODO: need to add is_deleted and filter in ingredients deleted from grocery list
    List<IngredientEntity> getIngredientsNotInGrocery(long groceryId, String search);

    @Query("SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id, is_checked, quantity, quantity_meas_id" +
            "       FROM " +
            "       (SELECT groceryId, i.ingredientId, i.onlineIngredientId, i.ingredientName, i.food_type_id, " +
            "           GIE.quantity, GIE.quantity_meas_id" +
            "           FROM IngredientEntity i " +
            "           JOIN GroceryIngredientEntity GIE " +
            "           USING (ingredientId) " +
            "           WHERE GIE.groceryId = :groceryId)" +
            "       JOIN " +
            "           (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge GIB WHERE groceryId = :groceryId) " +
            "       USING (groceryId, ingredientId) ") List<GroceryIngredientsTuple> getGroceryIngredients(long groceryId);

    @Query("SELECT recipeId, onlineRecipeId, groceryId, onlineGroceryId, recipeName, ingredientId, onlineIngredientId, ingredientName, amount," +
            " food_type_id, quantity, quantity_meas_id, is_checked, amount " +
            "       FROM " +
                        /* Get ingredient values */
            "           (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id FROM IngredientEntity) " +
            "           JOIN" +
                        /* Get is_checked */
            "           (SELECT groceryId, ingredientId, recipeId, onlineRecipeId, recipeName, amount, ingredientId, quantity, quantity_meas_id, is_checked " +
            "               FROM " +
            "               (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge)" +
            "               JOIN " +
                            /* Get recipe name */
            "               (SELECT groceryId, recipeId, onlineRecipeId, name AS recipeName, amount, ingredientId, quantity, quantity_meas_id " +
            "                   FROM " +
                                /* get amount and quantities of recipe ingredients */
            "                   (SELECT groceryId, recipeId, amount, ingredientId, quantity, quantity_meas_id " +
            "                       FROM (SELECT groceryId, recipeId, ingredientId, quantity, quantity_meas_id " +
            "                           FROM GroceryRecipeIngredientEntity WHERE groceryId=:groceryId)" +
            "                       JOIN (SELECT amount, recipeId FROM GroceryRecipeBridge WHERE groceryId=:groceryId)" +
            "                       USING (recipeId)) " +
            "                   JOIN (SELECT recipeId, onlineRecipeId, name FROM recipeEntity)" +
            "                   USING (recipeId))" +
            "               USING (groceryId, ingredientId))" +
            "           USING (ingredientId)" +
            "           JOIN GroceryEntity USING (groceryId)")
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

    @Query("SELECT * FROM GroceryEntity WHERE name like :grocerySearch")
    List<GroceryEntity> searchGroceries(String grocerySearch);

    @Query("SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id, is_checked, quantity, quantity_meas_id " +
            "       FROM " +
            "       (SELECT groceryId, i.ingredientId, i.onlineIngredientId, i.ingredientName, i.food_type_id, " +
            "           GIE.quantity, GIE.quantity_meas_id" +
            "           FROM IngredientEntity i " +
            "           JOIN GroceryIngredientEntity GIE " +
            "           USING (ingredientId) " +
            "           WHERE GIE.groceryId = :groceryId AND ingredientName LIKE :ingredientSearch)" +
            "       JOIN " +
            "           (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge GIB WHERE groceryId = :groceryId) " +
            "       USING (groceryId, ingredientId)")
    List<GroceryIngredientsTuple> searchGroceryIngredients(long groceryId, String ingredientSearch);

    @Query("SELECT recipeId, onlineRecipeId, groceryId, onlineGroceryId, recipeName, ingredientId, ingredientName, amount, " +
            " food_type_id, quantity, quantity_meas_id, is_checked, amount " +
            "       FROM " +
            /* Get ingredient values */
            "           (SELECT ingredientId, ingredientName, food_type_id FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch) " +
            "           JOIN" +
            /* Get is_checked */
            "           (SELECT groceryId, ingredientId, recipeId, onlineRecipeId, recipeName, amount, ingredientId, quantity, quantity_meas_id, is_checked " +
            "               FROM " +
            "               (SELECT groceryId, ingredientId, is_checked FROM GroceryIngredientBridge)" +
            "               JOIN " +
            /* Get recipe name */
            "               (SELECT groceryId, recipeId, onlineRecipeId, name AS recipeName, amount, ingredientId, quantity, quantity_meas_id " +
            "                   FROM " +
            /* get amount and quantities of recipe ingredients */
            "                   (SELECT groceryId, recipeId, amount, ingredientId, quantity, quantity_meas_id " +
            "                       FROM (SELECT groceryId, recipeId, ingredientId, quantity, quantity_meas_id FROM GroceryRecipeIngredientEntity WHERE groceryId=:groceryId)" +
            "                       JOIN (SELECT amount, recipeId FROM GroceryRecipeBridge WHERE groceryId=:groceryId)" +
            "                       USING (recipeId)) " +
            "                   JOIN recipeEntity USING (recipeId))" +
            "               USING (groceryId, ingredientId))" +
            "           USING (ingredientId)" +
            "           JOIN GroceryEntity USING (groceryId)")
    List<RecipeIngredientInGroceryTuple> searchRecipeIngredientsInGrocery(long groceryId, String ingredientSearch);

}
