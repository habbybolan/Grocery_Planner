package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

/**
 * Sql commands dealing with Ingredient queries.
 */
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(IngredientEntity ingredientEntity);

    @Delete
    void deleteIngredient(IngredientEntity ingredientEntity);

}
