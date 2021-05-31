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

    public RecipeTagBridge(long recipeId, long tagId, Timestamp dateUpdated, Timestamp dateSynchronized) {
        this.recipeId = recipeId;
        this.tagId = tagId;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    @Ignore
    public RecipeTagBridge(long recipeId, long tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }
}
