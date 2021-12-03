package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.sql.Timestamp;

@Entity(foreignKeys = {
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                childColumns = "unit_of_measurement_id",
                parentColumns = "id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = RecipeEntity.class,
                childColumns = "recipe_id",
                parentColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = NutritionEntity.class,
                childColumns = "nutrition_id",
                parentColumns = "nutrition_id",
                onDelete = ForeignKey.CASCADE),
},
    primaryKeys = {"recipe_id", "nutrition_id"})
public class RecipeNutritionBridge {

    @ColumnInfo(name = "recipe_id")
    public long recipeId;
    @ColumnInfo(name = "nutrition_id")
    public long nutritionId;
    public int amount;
    @ColumnInfo(name = "unit_of_measurement_id", index = true)
    public Long unitOfMeasurementId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    public boolean isDeleted;

    public RecipeNutritionBridge(long recipeId, long nutritionId, int amount, Long unitOfMeasurementId,
                                 Timestamp dateUpdated, Timestamp dateSynchronized, boolean isDeleted) {
        this.recipeId = recipeId;
        this.nutritionId = nutritionId;
        this.amount = amount;
        this.unitOfMeasurementId = unitOfMeasurementId;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
        this.isDeleted = isDeleted;
    }

    @Ignore
    public RecipeNutritionBridge(long recipeId, long nutritionId, int amount, Long unitOfMeasurementId) {
        this.recipeId = recipeId;
        this.nutritionId = nutritionId;
        this.amount = amount;
        this.unitOfMeasurementId = unitOfMeasurementId;
    }

    public void setDateSynchronized(Timestamp dateSynchronized) {
        this.dateSynchronized = dateSynchronized;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
