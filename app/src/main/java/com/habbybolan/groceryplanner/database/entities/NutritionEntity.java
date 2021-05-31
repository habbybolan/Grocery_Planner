package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NutritionEntity {

    @PrimaryKey
    @ColumnInfo(name = "nutrition_id")
    public long nutritionId;
    @ColumnInfo(name = "nutrition_name")
    public String nutritionName;
}
