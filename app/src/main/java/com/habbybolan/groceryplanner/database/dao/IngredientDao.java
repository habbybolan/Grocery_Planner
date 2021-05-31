package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

import java.util.List;

/**
 * Sql commands dealing with Ingredient queries.
 */
@Dao
public abstract class IngredientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertIngredient(IngredientEntity ingredientEntity);

    @Update
    public abstract void updateIngredient(IngredientEntity ingredientEntity);

    /**
     * Updates the ingredient if the id != 0 or the name already exists as an ingredient. Nothing to update.
     * Otherwise, it's a new Ingredient, so insert it.
     * @param ingredientEntity  Ingredient to insert or update
     */
    @Transaction
    public void insertUpdateIngredient(IngredientEntity ingredientEntity) {
        // if no explicit id set, check if that ingredient already exists
        if (ingredientEntity.getIngredientId() == 0) {
            long id = hasIngredientName(ingredientEntity.getIngredientName());
            // if not 0, then the ingredient already exists
            if (id == 0) {
                insertIngredient(ingredientEntity);
            }
        }
    }

    @Query("SELECT ingredientName FROM IngredientEntity WHERE ingredientName = :name LIMIT 1")
    public abstract long hasIngredientName(String name);

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName=:name")
    public abstract IngredientEntity getIngredient(String name);

    @Query("DELETE FROM IngredientEntity WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredient(List<Long> ingredientIds);

    @Query("DELETE FROM RecipeIngredientBridge WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredientsFromRecipeIngredientBridge(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredientsFromGroceryRecipeIngredientEntity(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryIngredientEntity WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredientsFromGroceryIngredientEntity(List<Long> ingredientIds);

    @Query("DELETE FROM GroceryIngredientBridge WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredientsFromGroceryIngredientBridge(List<Long> ingredientIds);

    @Query("SELECT * FROM IngredientEntity")
    public abstract List<IngredientEntity> getIngredients();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName ASC")
    public abstract List<IngredientEntity> getIngredientsSortAlphabeticalAsc();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName DESC")
    public abstract List<IngredientEntity> getIngredientsSortAlphabeticalDesc();

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch")
    public abstract List<IngredientEntity> searchIngredients(String ingredientSearch);
}
