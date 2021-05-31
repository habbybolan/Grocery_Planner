package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

import java.sql.Timestamp;

public class RecipeNutritionTuple {
    @ColumnInfo(name = "nutrition_id")
    public long nutritionId;
    @ColumnInfo(name = "amount")
    public int amount;
    @ColumnInfo(name = "unit_of_measurement_id")
    public Long unitOfMeasurementId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
}
