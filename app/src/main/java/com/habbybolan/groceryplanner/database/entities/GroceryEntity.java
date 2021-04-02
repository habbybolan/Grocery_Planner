package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

@Entity
public class GroceryEntity {
    @PrimaryKey(autoGenerate = true)
    private long groceryId;
    private String name;
    @ColumnInfo(name="is_favorite")
    private boolean isFavorite;

    public GroceryEntity(long groceryId, String name, boolean isFavorite) {
        this.groceryId = groceryId;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public GroceryEntity(Grocery grocery) {
        groceryId = grocery.getId();
        name = grocery.getName();
        isFavorite = grocery.getIsFavorite();
    }

    public long getGroceryId() {
        return groceryId;
    }
    public String getName() {
        return name;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
}
