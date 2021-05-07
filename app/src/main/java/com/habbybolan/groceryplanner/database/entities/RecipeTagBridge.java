package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

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

    public RecipeTagBridge(long recipeId, long tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }
}
