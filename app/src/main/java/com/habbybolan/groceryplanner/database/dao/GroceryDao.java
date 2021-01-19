package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.habbybolan.groceryplanner.database.relations.GroceryWithIngredients;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;

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

    @Query("DELETE FROM GroceryIngredientBridge WHERE groceryId = :groceryId")
    void deleteGroceryFromBridge(long groceryId);

    // Bridge table access
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntoBridge(GroceryIngredientBridge groceryIngredientBridge);

    @Delete
    void deleteFromBridge(GroceryIngredientBridge groceryIngredientBridge);

    /*
    @Transaction
    @Query("SELECT * FROM GroceryIngredientView WHERE groceryId = :groceryId")
    List<GroceryIngredientView> getIngredientsFromGrocery(long groceryId);
    */

    @Transaction
    @Query("SELECT * FROM GroceryEntity WHERE groceryId = :groceryId LIMIT 1")
    GroceryWithIngredients getIngredientsFromGrocery(long groceryId);

    @Query("SELECT * FROM GroceryIngredientBridge WHERE groceryId = :groceryId")
    List<GroceryIngredientBridge> getGroceryIngredient(long groceryId);
}
