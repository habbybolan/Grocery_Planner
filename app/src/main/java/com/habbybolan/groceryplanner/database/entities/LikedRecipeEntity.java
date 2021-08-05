package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys =
        {@ForeignKey(entity = RecipeEntity.class,
                childColumns = "recipeId",
                parentColumns = "recipeId")})
public class LikedRecipeEntity {

    @PrimaryKey
    public long id;
    @ColumnInfo(index = true)
    public long recipeId;
}
