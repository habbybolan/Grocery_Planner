package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

import java.util.List;

/**
 * Sql commands dealing with Ingredient queries.
 */
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertIngredient(IngredientEntity ingredientEntity);

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName=:name")
    IngredientEntity getIngredient(String name);

    @Update
    void updateIngredient(IngredientEntity ingredientEntity);

    @Query("DELETE FROM IngredientEntity WHERE ingredientId IN (:ingredientIds)")
    void deleteIngredient(List<Long> ingredientIds);

    @Query("DELETE FROM RecipeIngredientBridge WHERE ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromRecipeIngredientBridge(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromGroceryRecipeIngredientEntity(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryIngredientEntity WHERE ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromGroceryIngredientEntity(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryIngredientBridge WHERE ingredientId IN (:ingredientIds)")
    void deleteIngredientsFromGroceryIngredientBridge(List<Long> ingredientIds);

    @Query("SELECT * FROM IngredientEntity")
    List<IngredientEntity> getIngredients();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName ASC")
    List<IngredientEntity> getIngredientsSortAlphabeticalAsc();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName DESC")
    List<IngredientEntity> getIngredientsSortAlphabeticalDesc();

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch")
    List<IngredientEntity> searchIngredients(String ingredientSearch);
}
