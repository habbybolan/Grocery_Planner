package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.models.databasetuples.CountTuple;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.sql.Timestamp;
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
    public long insertUpdateIngredient(IngredientEntity ingredientEntity) {
        // if no explicit id set, check if that ingredient already exists
        long id;
        if (ingredientEntity.getIngredientId() == 0) {
            id = hasIngredientName(ingredientEntity.getIngredientName()).count;
            // if 0, then ingredient doesn't exist
            if (id <= 0) {
                id = insertIngredient(ingredientEntity);
            } else {
                ingredientEntity.setIngredientId(id);
                updateIngredient(ingredientEntity);
            }
        } else {
            id = ingredientEntity.getIngredientId();
            updateIngredient(ingredientEntity);
        }
        return id;
    }

    /**
     * Insert or update the Ingredients into the IngredientEntity table, depending on if it exists yet or not.
     * Insert the relationship between the Ingredient and recipeId inside the RecipeIngredientBridge Table.
     * @param ingredients       List of ingredients to insert/update and create the relationship between recipeId and ingredient.
     * @param recipeId          The id of the recipe to insert the ingredients into
     */
    @Transaction
    public void insertUpdateIngredientsIntoRecipe(List<Ingredient> ingredients, long recipeId) {
        for (Ingredient ingredient : ingredients) {
            IngredientEntity ingredientEntity = new IngredientEntity(ingredient);
            // if id = 0 then ingredient doesn't exist, so look if an ingredient of that name exists, otherwise create a new Ingredient
            long ingredientId = insertUpdateIngredient(ingredientEntity);
            // set in case the ingredient was just inserted
            ingredientEntity.setIngredientId(ingredientId);
            long id = insertIngredientBridge(new RecipeIngredientBridge(recipeId, ingredientEntity.getIngredientId(), ingredient.getQuantity(), ingredient.getQuantityMeasId()));
            // if ingredient recipe relationship exists, update it
            if (id <= 0) {
                updateIngredientBridge(recipeId, ingredientEntity.getIngredientId(), ingredient.getQuantity(), ingredient.getQuantityMeasId());
            }
        }
    }

    @Transaction
    public long insertIngredientBridge(RecipeIngredientBridge recipeIngredientBridge) {
        long id = insertIngredientBridgeHelper(recipeIngredientBridge);
        setDateUpdatedRecipeIngredientBridge(recipeIngredientBridge.recipeId, recipeIngredientBridge.ingredientId);
        return id;
    }

    @Query("UPDATE recipeingredientbridge SET date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void setDateUpdatedRecipeIngredientBridge(long recipeId, long ingredientId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertIngredientBridgeHelper(RecipeIngredientBridge recipeIngredientBridge);

    @Query("Update RecipeIngredientBridge SET" +
            "   quantity = :quantity," +
            "   quantity_meas_id = :quantityMeasId," +
            "   date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
            "   WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void updateIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId);

    @Query("SELECT COUNT(*) AS count FROM IngredientEntity WHERE ingredientName = :name LIMIT 1")
    public abstract CountTuple hasIngredientName(String name);

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName=:name")
    public abstract IngredientEntity getIngredient(String name);

    @Query("DELETE FROM IngredientEntity WHERE ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredient(List<Long> ingredientIds);

    @Query("SELECT * FROM IngredientEntity")
    public abstract List<IngredientEntity> getIngredients();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName ASC")
    public abstract List<IngredientEntity> getIngredientsSortAlphabeticalAsc();

    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientName DESC")
    public abstract List<IngredientEntity> getIngredientsSortAlphabeticalDesc();

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch")
    public abstract List<IngredientEntity> searchIngredients(String ingredientSearch);

    @Transaction
    public void syncIngredientInsert(IngredientEntity ingredientEntity, RecipeIngredientBridge recipeIngredientBridge) {
        long ingredientId = insertUpdateIngredient(ingredientEntity);
        ingredientEntity.setIngredientId(ingredientId);
        recipeIngredientBridge.setIngredientId(ingredientId);
        syncInsertIngredientBridge(recipeIngredientBridge);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void syncInsertIngredientBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Transaction
    public void syncIngredientUpdate(IngredientEntity ingredientEntity, long recipeId, long ingredientId, float quantity, Long measurementId, Timestamp dateSynchronized, boolean isDeleted) {
        updateIngredient(ingredientEntity);
        syncIngredientBridgeUpdate(recipeId, ingredientId, quantity, measurementId, dateSynchronized, isDeleted);
    }

    @Query("UPDATE recipeingredientbridge SET quantity = :quantity AND quantity_meas_id = :measurementId AND date_synchronized = :dateSynchronized AND is_deleted = :isDeleted" +
            "   WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    abstract void syncIngredientBridgeUpdate(long recipeId, long ingredientId, float quantity, Long measurementId, Timestamp dateSynchronized, boolean isDeleted);

    @Transaction
    public void syncIngredientInsertedOnline(long recipeId, long ingredientId, long onlineIngredientId, Timestamp dateSync) {
        syncIngredientUpdateDateSync(recipeId, ingredientId, dateSync);
        updateIngredientOnlineId(ingredientId, onlineIngredientId);
    }

    @Query("UPDATE ingrediententity SET onlineIngredientId = :onlineIngredientId WHERE ingredientId = :ingredientId")
    abstract void updateIngredientOnlineId(long ingredientId, long onlineIngredientId);

    @Query("UPDATE recipeingredientbridge SET date_synchronized = :dateSync WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void syncIngredientUpdateDateSync(long recipeId, long ingredientId, Timestamp dateSync);
}
