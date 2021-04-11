package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

import java.util.List;

/**
 * Sql commands dealing with Ingredient queries.
 */
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(IngredientEntity ingredientEntity);

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
}
