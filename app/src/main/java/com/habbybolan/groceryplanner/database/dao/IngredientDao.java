package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.models.databasetuples.CountTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
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

    @Query("UPDATE IngredientEntity SET food_type_id = :foodTypeId WHERE ingredientId = :ingredientId")
    public abstract void updateIngredient(long foodTypeId, long ingredientId);

    /**
     * If the id exists inside ingredientEntity param, the update the existing ingredient
     * If the id doesn't exist
     *      if the name exists in database, only get the id of the existing ingredient
     *          and set IngredientEntity id to 0 as signal
     *      otherwise, insert the new ingredient into database
     * @param ingredientEntity  Ingredient to update/insert if applicable
     */
    @Transaction
    public long insertUpdateIngredient(IngredientEntity ingredientEntity) {
        // if no explicit id set, check if that ingredient already exists
        long id;
        if (ingredientEntity.getIngredientId() == 0) {
            // check if the name already exists in the database
            String name = ingredientEntity.getIngredientName();
            if (hasIngredientName(name).count > 0) {
                // get existing ingredient id and don't update
                id = getIngredientByName(name).getIngredientId();
                // set id to 0 to signal that tried to insert an ingredient that already exists
                ingredientEntity.setIngredientId(0);
            } else {
                // otherwise store a new ingredient
                id = insertIngredient(ingredientEntity);
                ingredientEntity.setIngredientId(id);
            }
        } else {
            // ingredientEntity already contains the database id, update with new values
            id = ingredientEntity.getIngredientId();
            updateIngredient(ingredientEntity.getFoodTypeId(), id);
        }
        // return the id of the ingredient
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
            insertUpdateIngredientIntoRecipe(ingredient, recipeId);
        }
    }

    @Transaction
    public long insertUpdateIngredientIntoRecipe(Ingredient ingredient, long recipeId) {
        IngredientEntity ingredientEntity = new IngredientEntity(ingredient);
        long id = insertUpdateIngredient(ingredientEntity);
        RecipeIngredientsTuple tuple = getFullRecipeIngredientById(id, recipeId);
        if (tuple != null) {
            // then relationship already exists
            if (ingredientEntity.getIngredientId() == 0) {
                // then tried to insert an ingredient that exists and relationship already exists, do nothing
            } else {
                // otherwise, update relationship
                updateIngredientBridge(recipeId, id, ingredient.getQuantity(), ingredient.getQuantityMeasId());
            }
        } else {
            // otherwise insert relationship
            insertRecipeIngredientBridge(new RecipeIngredientBridge(recipeId, id, ingredient.getQuantity(), ingredient.getQuantityMeasId()));
        }
        return id;
    }

    @Transaction
    public void insertRecipeIngredientBridge(RecipeIngredientBridge recipeIngredientBridge) {
        insertRecipeIngredientBridgeHelper(recipeIngredientBridge);
        setDateUpdatedRecipeIngredientBridge(recipeIngredientBridge.recipeId, recipeIngredientBridge.ingredientId);
    }

    @Query("UPDATE recipeingredientbridge SET date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    abstract void setDateUpdatedRecipeIngredientBridge(long recipeId, long ingredientId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertRecipeIngredientBridgeHelper(RecipeIngredientBridge recipeIngredientBridge);

    @Query("Update RecipeIngredientBridge SET" +
            "   quantity = :quantity," +
            "   quantity_meas_id = :quantityMeasId," +
            "   is_deleted = 0," +
            "   date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
            "   WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void updateIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId);

    @Query("SELECT COUNT(*) AS count FROM IngredientEntity WHERE ingredientName = :name LIMIT 1")
    public abstract CountTuple hasIngredientName(String name);

    @Query("SELECT * FROM IngredientEntity WHERE ingredientName=:name")
    public abstract IngredientEntity getIngredientByName(String name);

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
        updateIngredient(ingredientEntity.getFoodTypeId(), ingredientId);
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

    @Query("SELECT * FROM " +
            "(SELECT * FROM IngredientEntity) AS I " +
            "INNER JOIN " +
            "(SELECT * FROM RecipeIngredientBridge WHERE ingredientId = :ingredient AND recipeId = :recipeId) AS RIB " +
            "USING (ingredientId)")
    public abstract RecipeIngredientsTuple getFullRecipeIngredientById(long ingredient, long recipeId);
}
