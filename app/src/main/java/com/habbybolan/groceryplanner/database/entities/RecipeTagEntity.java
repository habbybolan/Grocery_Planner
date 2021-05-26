package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"title"}, unique = true)})
public class RecipeTagEntity {

    @PrimaryKey(autoGenerate = true)
    public long tagId;
    public Long onlineTagId;
    public String title;

    public RecipeTagEntity(long tagId, Long onlineTagId, String title) {
        this.tagId = tagId;
        this.onlineTagId = onlineTagId;
        this.title = title;
    }
}
