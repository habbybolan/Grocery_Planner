package com.habbybolan.groceryplanner.models.databasetuples;

import androidx.room.ColumnInfo;

import java.sql.Timestamp;

public class RecipeTagTuple {

    public long tagId;
    public String title;
    public Long onlineTagId;
    @ColumnInfo(name = "date_synchronized", index = true)
    public Timestamp dateSynchronized;
    @ColumnInfo(name = "date_updated", index = true)
    public Timestamp dateUpdated;
}
