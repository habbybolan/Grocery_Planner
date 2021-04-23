package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"title"}, unique = true)})
public class RecipeTagEntity {

    @PrimaryKey(autoGenerate = true)
    public long tagId;
    public String title;

    public RecipeTagEntity(long tagId, String title) {
        this.tagId = tagId;
        this.title = title;
    }
}
