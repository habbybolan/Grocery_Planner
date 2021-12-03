package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.sql.Timestamp;

@Entity(primaryKeys = {"recipeId", "tagId"}, foreignKeys = {
        @ForeignKey(entity = RecipeTagEntity.class,
                parentColumns = "tagId",
                childColumns = "tagId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = RecipeEntity.class,
                parentColumns = "recipeId",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE)
})
public class RecipeTagBridge {

    @ColumnInfo(index = true)
    public long recipeId;
    @ColumnInfo(index = true)
    public long tagId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    public boolean isDeleted;

    public RecipeTagBridge(long recipeId, long tagId, Timestamp dateUpdated,
                           Timestamp dateSynchronized, boolean isDeleted) {
        this.recipeId = recipeId;
        this.tagId = tagId;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
        this.isDeleted = isDeleted;
    }

    @Ignore
    public RecipeTagBridge(long recipeId, long tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }

    public void setDateSynchronized(Timestamp date) {
        this.dateSynchronized = date;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
