package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

@Entity
public class GroceryEntity {
    @PrimaryKey(autoGenerate = true)
    private long groceryId;
    private String name;

    public GroceryEntity(long groceryId, String name) {
        this.groceryId = groceryId;
        this.name = name;
    }

    public GroceryEntity(Grocery grocery) {
        groceryId = grocery.getId();
        name = grocery.getName();
    }

    public long getGroceryId() {
        return groceryId;
    }
    public String getName() {
        return name;
    }
}
