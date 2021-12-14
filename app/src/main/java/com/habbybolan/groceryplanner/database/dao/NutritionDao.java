package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.habbybolan.groceryplanner.database.entities.RecipeNutritionBridge;

import java.sql.Timestamp;

/**
 * Sql commands dealing with Nutrition queries.
 */
@Dao
public abstract class NutritionDao {

    @Transaction
    public void insertUpdateNutritionBridge(RecipeNutritionBridge bridge) {
        insertNutritionBridge(bridge);
        updateNutritionBridge(bridge.recipeId, bridge.nutritionId, bridge.amount, bridge.unitOfMeasurementId);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertNutritionBridge(RecipeNutritionBridge bridge);

    @Query("UPDATE RecipeNutritionBridge SET" +
            "   amount = :amount," +
            "   unit_of_measurement_id = :unitOfMeasurementId," +
            "   is_deleted = 0," +
            "   date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
            "   WHERE recipe_id = :recipeId AND nutrition_id = :nutritionId")
    public abstract void updateNutritionBridge(long recipeId, long nutritionId, int amount, Long unitOfMeasurementId);

    @Transaction
    public void syncNutritionBridgeInsert(RecipeNutritionBridge recipeNutritionBridge) {
        insertNutritionBridge(recipeNutritionBridge);
    }

    @Transaction
    public void syncNutritionBridgeUpdate(long recipeId, long nutritionId, int amount, Long measurementId, Timestamp dateSynchronized, boolean isDeleted) {
        syncNutritionBridgeUpdateHelper(recipeId, nutritionId, amount, measurementId, dateSynchronized, isDeleted);
    }

    @Query("UPDATE recipenutritionbridge SET amount = :amount AND unit_of_measurement_id = :measurementId AND date_synchronized = :dateSynchronized AND is_deleted = :isDeleted" +
            "   WHERE recipe_id = :recipeId AND nutrition_id = :nutritionId")
    public abstract void syncNutritionBridgeUpdateHelper(long recipeId, long nutritionId, int amount, Long measurementId, Timestamp dateSynchronized, boolean isDeleted);


    @Query("UPDATE RecipeNutritionBridge SET date_synchronized = :dateSync WHERE recipe_id = :recipeId AND nutrition_id = :nutritionId")
    public abstract void syncNutritionUpdateDateSync(long recipeId, long nutritionId, Timestamp dateSync);
}
