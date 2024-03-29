package com.habbybolan.groceryplanner.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.sql.Timestamp;

@Entity(primaryKeys = {"recipeId", "ingredientId"}, foreignKeys = {
        @ForeignKey(entity = RecipeEntity.class,
                parentColumns = "recipeId",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = IngredientEntity.class,
                parentColumns = "ingredientId",
                childColumns = "ingredientId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "quantity_meas_id")
})
public class  RecipeIngredientBridge {

    @ColumnInfo(index = true)
    public long recipeId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public float quantity;
    @ColumnInfo(name="quantity_meas_id", index = true)
    public Long quantityMeasId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
    @ColumnInfo(name = "is_deleted")
    public boolean isDeleted;

    public RecipeIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId,
                                  Timestamp dateUpdated, Timestamp dateSynchronized, boolean isDeleted) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
        this.isDeleted = isDeleted;
    }

    @Ignore
    public RecipeIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
    }

    public void setDateSynchronized(Timestamp date) {
        this.dateSynchronized = date;
    }
    public void setDateUpdated(Timestamp date) {
        this.dateUpdated = date;
    }
    public void setIngredientId(long id) {
        this.ingredientId = id;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
