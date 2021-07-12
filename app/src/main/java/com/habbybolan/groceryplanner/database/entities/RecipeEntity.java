package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.sql.Timestamp;

@Entity(foreignKeys = {
        @ForeignKey(entity = RecipeCategoryEntity.class,
                parentColumns = "recipeCategoryId",
                childColumns = "recipe_category_id",
                onDelete = ForeignKey.SET_NULL)
})
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private long recipeId;
    private Long onlineRecipeId;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="is_favorite")
    private boolean isFavorite;
    @ColumnInfo(defaultValue = "")
    private String description;
    @ColumnInfo(name="prep_time")
    private int prepTime;
    @ColumnInfo(name="cook_time")
    private int cookTime;
    @ColumnInfo(name="serving_size")
    private int servingSize;
    @ColumnInfo(name = "recipe_category_id", index = true)
    private Long recipeCategoryId;
    @ColumnInfo(name = "instructions", defaultValue = "")
    private String instructions;
    @ColumnInfo(name = "date_created", index = true)
    private Timestamp dateCreated;
    @ColumnInfo(name = "date_synchronized", index = true)
    private Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    private Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    public RecipeEntity(long recipeId, Long onlineRecipeId, String name, boolean isFavorite, String description,
                        int prepTime, int cookTime, int servingSize, Long recipeCategoryId, String instructions,
                        Timestamp dateCreated, Timestamp dateSynchronized, Timestamp dateUpdated, boolean isDeleted) {
        this.recipeId = recipeId;
        this.onlineRecipeId = onlineRecipeId;
        this.name = name;
        this.isFavorite = isFavorite;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servingSize = servingSize;
        this.recipeCategoryId = recipeCategoryId;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
        this.dateSynchronized = dateSynchronized;
        this.dateUpdated = dateUpdated;
        this.isDeleted = isDeleted;
    }

    public RecipeEntity(OfflineRecipe offlineRecipe) {
        recipeId = offlineRecipe.getId();
        onlineRecipeId = offlineRecipe.getOnlineId();
        name = offlineRecipe.getName();
        isFavorite = offlineRecipe.getIsFavorite();

        description = offlineRecipe.getDescription();
        prepTime = offlineRecipe.getPrepTime();
        cookTime = offlineRecipe.getCookTime();
        servingSize = offlineRecipe.getServingSize();

        recipeCategoryId = offlineRecipe.getCategoryId();
        instructions = offlineRecipe.getInstructions();
        dateCreated = offlineRecipe.getDateCreated();
        dateSynchronized = offlineRecipe.getDateSynchronized();
        dateUpdated = offlineRecipe.getDateUpdated();
    }


    public long getRecipeId() {
        return recipeId;
    }
    public Long getOnlineRecipeId() {
        return onlineRecipeId;
    }
    public String getName() {
        return name;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public String getDescription() {
        return description;
    }
    public int getPrepTime() {
        return prepTime;
    }
    public int getCookTime() {
        return cookTime;
    }
    public int getServingSize() {
        return servingSize;
    }
    public Long getRecipeCategoryId() {
        return recipeCategoryId;
    }
    public String getInstructions() {
        return instructions;
    }
    public Timestamp getDateCreated() {
        return dateCreated;
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

    public static String getNameColumn() {
        return "name";
    }
    public static String getDateColumn() {
        return "date_created";
    }
}
