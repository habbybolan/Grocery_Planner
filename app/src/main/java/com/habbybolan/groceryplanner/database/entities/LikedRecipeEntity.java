package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(foreignKeys =
        {@ForeignKey(entity = RecipeEntity.class,
                childColumns = "recipeId",
                parentColumns = "recipeId")})
public class LikedRecipeEntity {

    public LikedRecipeEntity(long id, long recipeId, Timestamp dateSynchronized, Timestamp dateUpdated, boolean isDeleted) {
        this.id = id;
        this.recipeId = recipeId;
        this.dateSynchronized = dateSynchronized;
        this.dateUpdated = dateUpdated;
        this.isDeleted = isDeleted;
    }

    @PrimaryKey
    public long id;
    @ColumnInfo(index = true)
    public long recipeId;

    @ColumnInfo(name = "date_synchronized", index = true)
    private Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    private Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
