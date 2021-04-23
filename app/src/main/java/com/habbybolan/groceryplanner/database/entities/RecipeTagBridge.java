package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"recipeId", "tagId"})
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
