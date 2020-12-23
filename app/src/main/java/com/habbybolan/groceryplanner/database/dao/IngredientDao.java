package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

/**
 * Sql commands dealing with Ingredient queries.
 */
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredient(IngredientEntity ingredientEntity);

    @Delete
    void deleteIngredient(IngredientEntity ingredientEntity);

    @Query("SELECT * FROM ingrediententity WHERE ingredientName = :ingredientName")
    IngredientEntity getIngredient(String ingredientName);
}
