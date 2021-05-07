package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UnitOfMeasurementEntity {

    @PrimaryKey
    public long id;
    public String type;
    @ColumnInfo(name = "type_code")
    public String typeCode;

    public UnitOfMeasurementEntity(long id, String type, String typeCode) {
        this.id = id;
        this.type = type;
        this.typeCode = typeCode;
    }
}
