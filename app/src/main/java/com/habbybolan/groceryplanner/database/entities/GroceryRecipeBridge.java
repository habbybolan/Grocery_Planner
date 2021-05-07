package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"groceryId", "recipeId"}, foreignKeys = {
        @ForeignKey(entity = GroceryEntity.class,
                parentColumns = "groceryId",
                childColumns = "groceryId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = RecipeEntity.class,
                parentColumns = "recipeId",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE)
})
public class GroceryRecipeBridge {

    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long recipeId;
    public int amount;

    public GroceryRecipeBridge(long groceryId, long recipeId, int amount) {
        this.groceryId = groceryId;
        this.recipeId = recipeId;
        this.amount = amount;
    }
}
