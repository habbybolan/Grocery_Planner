package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodTypeEntity {

    @PrimaryKey
    @ColumnInfo(name = "food_type_id")
    public long foodTypeId;
    public String name;
}
