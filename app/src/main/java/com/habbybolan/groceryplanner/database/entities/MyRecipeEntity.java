package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys =
        {@ForeignKey(entity = RecipeEntity.class,
                childColumns = "recipeId",
                parentColumns = "recipeId"),
        @ForeignKey(entity = AccessLevelEntity.class,
                childColumns = "accessLevelId",
                parentColumns = "id")
})
public class MyRecipeEntity {

    public MyRecipeEntity(long recipeId, long accessLevelId) {
        this.recipeId = recipeId;
        this.accessLevelId = accessLevelId;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(index = true)
    public long recipeId;
    public long accessLevelId;
    // todo: implement date synchronized when implementing synchronization
    //@ColumnInfo(name = "date_synchronized", index = true)
    //private Timestamp dateSynchronized;
}
