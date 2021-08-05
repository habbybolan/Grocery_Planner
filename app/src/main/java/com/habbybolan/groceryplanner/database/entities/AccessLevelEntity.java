package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AccessLevelEntity {

    @PrimaryKey
    public int id;
    public String title;
}
