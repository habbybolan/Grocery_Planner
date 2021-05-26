package com.habbybolan.groceryplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import java.sql.Timestamp;

@Entity
public class GroceryEntity {
    @PrimaryKey(autoGenerate = true)
    private long groceryId;
    private Long onlineGroceryId;
    private String name;
    private Timestamp dateSynchronized;

    public GroceryEntity(long groceryId, Long onlineGroceryId, String name, Timestamp dateSynchronized) {
        this.groceryId = groceryId;
        this.onlineGroceryId = onlineGroceryId;
        this.name = name;
        this.dateSynchronized = dateSynchronized;
    }

    public GroceryEntity(Grocery grocery) {
        groceryId = grocery.getId();
        onlineGroceryId = grocery.getOnlineId();
        name = grocery.getName();
        dateSynchronized = grocery.getDateSynchronized();
    }

    public long getGroceryId() {
        return groceryId;
    }
    public Long getOnlineGroceryId() {
        return onlineGroceryId;
    }
    public String getName() {
        return name;
    }
    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }
}
