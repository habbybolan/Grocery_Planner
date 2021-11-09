package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(foreignKeys =
        {@ForeignKey(entity = RecipeEntity.class,
                childColumns = "recipeId",
                parentColumns = "recipeId"),
        @ForeignKey(entity = AccessLevelEntity.class,
                childColumns = "accessLevelId",
                parentColumns = "id")
})
public class MyRecipeEntity {

    @Ignore
    public MyRecipeEntity(long recipeId, long accessLevelId) {
        this.recipeId = recipeId;
        this.accessLevelId = accessLevelId;
    }

    public MyRecipeEntity(long id, long recipeId, long accessLevelId, Timestamp dateSynchronized, Timestamp dateUpdated, boolean isDeleted) {
        this.id = id;
        this.recipeId = recipeId;
        this.accessLevelId = accessLevelId;
        this.dateSynchronized = dateSynchronized;
        this.dateUpdated = dateUpdated;
        this.isDeleted = isDeleted;
    }

    @ColumnInfo(index = true)
    public long recipeId;
    public long accessLevelId;

    @ColumnInfo(name = "date_synchronized", index = true)
    private Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    private Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long getRecipeId() {
        return recipeId;
    }

    public long getAccessLevelId() {
        return accessLevelId;
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
