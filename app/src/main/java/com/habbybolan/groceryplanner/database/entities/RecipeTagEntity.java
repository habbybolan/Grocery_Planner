package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

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

    public RecipeTagEntity(RecipeTag recipeTag) {
        this.tagId = recipeTag.getId();
        this.onlineTagId = recipeTag.getOnlineId();
        this.title = recipeTag.getTitle();
    }
}
