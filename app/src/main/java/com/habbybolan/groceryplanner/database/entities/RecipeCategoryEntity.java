package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.RecipeCategory;

@Entity
public class RecipeCategoryEntity {

    @PrimaryKey(autoGenerate = true)
    private long recipeCategoryId;
    @ColumnInfo
    private String name;

    @Ignore
    public RecipeCategoryEntity(long recipeCategoryId, String name) {
        this.recipeCategoryId = recipeCategoryId;
        this.name = name;
    }

    public RecipeCategoryEntity(RecipeCategory recipeCategory) {
        recipeCategoryId = recipeCategory.getId();
        name = recipeCategory.getName();
    }

    public RecipeCategoryEntity(String name) {
        this.name = name;
    }

    public long getRecipeCategoryId() {
        return recipeCategoryId;
    }
    public void setRecipeCategoryId(long id) {
        this.recipeCategoryId = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}



