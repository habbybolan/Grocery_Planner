package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Sql commands dealing with recipe tag queries.
 */
@Dao
public abstract class RecipeTagDao {

    @Transaction
    public void insertUpdateRecipeTagsIntoRecipe(List<RecipeTagEntity> tags, long recipeId) {
        for (RecipeTagEntity tag : tags) {
            long tagId = insertUpdateRecipeTag(tag);
            tag.tagId = tagId;
            // associate the tag with the recipe
            RecipeTagBridge recipeTagBridge = new RecipeTagBridge(recipeId, tagId);
            insertUpdateRecipeTagBridge(recipeTagBridge);
        }
    }

    @Transaction
    public long insertUpdateTagIntoRecipe(RecipeTagEntity tagEntity, long recipeId) {
        long tagId = insertUpdateRecipeTag(tagEntity);
        RecipeTagBridge recipeTagBridge = new RecipeTagBridge(recipeId, tagId);
        insertUpdateRecipeTagBridge(recipeTagBridge);
        return tagId;
    }



    @Transaction
    public long insertUpdateRecipeTag(RecipeTagEntity tag) {
        long tagId;
        // if tag not associated with an already inserted one
        if (tag.tagId == 0) {
            // check a tag with the title already exists
            tagId = hasTagName(tag.title);
            // if not, insert new tag
            if (tagId <= 0) {
                tagId = insertTag(tag);
                // otherwise update existing
            } else {
                tag.tagId = tagId;
                updateTag(tag);
            }
            // otherwise, update tag
        } else {
            tagId = tag.tagId;
            updateTag(tag);
        }
        return tagId;
    }

    @Transaction
    void insertUpdateRecipeTagBridge(RecipeTagBridge recipeTagBridge) {
        long id = insertRecipeTagBridgeHelper(recipeTagBridge);
        if (id <= 0) {
            updateRecipeTagBridge(recipeTagBridge);
        }
        setDateUpdatedRecipeTagBridge(recipeTagBridge.recipeId, recipeTagBridge.tagId);
    }

    @Update
    abstract void updateRecipeTagBridge(RecipeTagBridge recipeTagBridge);


    @Query("UPDATE recipetagbridge SET date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') WHERE recipeId = :recipeId AND tagId = :tagId")
    public abstract void setDateUpdatedRecipeTagBridge(long recipeId, long tagId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertRecipeTagBridgeHelper(RecipeTagBridge bridge);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertTag(RecipeTagEntity tag);

    @Update
    public abstract void updateTag(RecipeTagEntity tag);

    @Query("SELECT tagId FROM RecipeTagEntity WHERE title = :name LIMIT 1")
    public abstract long hasTagName(String name);


    @Transaction
    public void syncTagInsert(RecipeTagEntity tagEntity, RecipeTagBridge recipeTagBridge) {
        long tagId = insertUpdateRecipeTag(tagEntity);
        tagEntity.tagId = tagId;
        recipeTagBridge.setTagId(tagId);
        syncInsertTagBridge(recipeTagBridge);
    }

    @Insert
    abstract void syncInsertTagBridge(RecipeTagBridge recipeTagBridge);

    @Transaction
    public void syncTagUpdate(RecipeTagEntity tagEntity, long recipeId, long tagId, Timestamp dateSynchronized, boolean isDeleted) {
        updateTag(tagEntity);
        syncTagBridgeUpdate(recipeId, tagId, dateSynchronized, isDeleted);
    }

    @Query("UPDATE recipetagbridge SET date_synchronized = :dateSynchronized AND is_deleted = :isDeleted" +
            "   WHERE recipeId = :recipeId AND tagId = :tagId")
    public abstract void syncTagBridgeUpdate(long recipeId, long tagId, Timestamp dateSynchronized, boolean isDeleted);

    @Transaction
    public void syncTagInsertedOnline(long recipeId, long tagId, long onlineTagId, Timestamp dateSync) {
        syncTagUpdateDateSync(recipeId, tagId, dateSync);
        updateTagOnlineId(tagId, onlineTagId);
    }

    @Query("UPDATE RecipeTagEntity SET onlineTagId = :onlineTagId WHERE tagId = :tagId")
    abstract void updateTagOnlineId(long tagId, long onlineTagId);

    @Query("UPDATE RecipeTagBridge SET date_synchronized = :dateSync WHERE recipeId = :recipeId AND tagId = :tagId")
    public abstract void syncTagUpdateDateSync(long recipeId, long tagId, Timestamp dateSync);

    @Query("UPDATE RecipeTagBridge SET is_deleted = 1 WHERE recipeId=:recipeId AND tagId = :tagId")
    abstract void deleteRecipeTagBridgeHelper(long recipeId, long tagId);

    @Transaction
    public void deleteRecipeTagBridge(long recipeId, long tagId) {
        deleteRecipeTagBridgeHelper(recipeId, tagId);
        setDateUpdatedRecipeTagBridge(recipeId, tagId);
    }

    /*@Transaction
    public void deleteRecipeTagByTitleBridge(long recipeId, String title) {
        deleteRecipeTagBridgeByTitleHelper(recipeId, title);
        setDateUpdatedRecipeTagBridge(recipeId, tagId);
    }

    @Query("UPDATE RecipeTagBridge SET is_deleted = 1" +
            "   WHERE recipeId = :recipeId AND" +
            "   tagId IN (SELECT tagId FROM RecipeTagEntity WHERE title = :title)")
    public abstract void deleteRecipeTagBridgeByTitleHelper(long recipeId, String title);*/
}
