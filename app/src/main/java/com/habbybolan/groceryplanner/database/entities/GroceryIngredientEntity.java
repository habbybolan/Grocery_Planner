package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"groceryId", "ingredientId"}, foreignKeys = {
        @ForeignKey(entity = GroceryEntity.class,
                parentColumns = "groceryId",
                childColumns = "groceryId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = IngredientEntity.class,
                parentColumns = "ingredientId",
                childColumns = "ingredientId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "quantity_meas_id")
})
public class GroceryIngredientEntity {

    @ColumnInfo(index = true)
    public long groceryId;
    @ColumnInfo(index = true)
    public long ingredientId;
    public float quantity;
    @ColumnInfo(name = "quantity_meas_id", index = true)
    public Long quantityMeasId;

    public GroceryIngredientEntity(long groceryId, long ingredientId, float quantity, Long quantityMeasId) {
        this.groceryId = groceryId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
    }
}
