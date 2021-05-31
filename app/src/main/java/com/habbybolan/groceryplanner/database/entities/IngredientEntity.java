package com.habbybolan.groceryplanner.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

@Entity(indices = {@Index(value = {"ingredientName"}, unique = true)},
    foreignKeys = {@ForeignKey(entity = FoodTypeEntity.class,
                    childColumns = "food_type_id",
                    parentColumns = "food_type_id",
                    onDelete = ForeignKey.CASCADE)})
public class IngredientEntity {

    @PrimaryKey(autoGenerate = true)
    private long ingredientId;
    private Long onlineIngredientId;
    @NonNull
    private String ingredientName;
    @ColumnInfo(name = "food_type_id", defaultValue = "0", index = true)
    private long foodTypeId;

    public IngredientEntity(long ingredientId, Long onlineIngredientId, @NonNull String ingredientName, long foodTypeId) {
        this.ingredientId = ingredientId;
        this.onlineIngredientId = onlineIngredientId;
        this.ingredientName = ingredientName;
        this.foodTypeId = foodTypeId;
    }

    public IngredientEntity(Ingredient ingredient) {
        ingredientId = ingredient.getId();
        onlineIngredientId = ingredient.getOnlineId();
        ingredientName = ingredient.getName();
        foodTypeId = ingredient.getFoodType().getId();
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public long getFoodTypeId() {
        return foodTypeId;
    }
    public long getIngredientId() {
        return ingredientId;
    }
    public Long getOnlineIngredientId() {
        return onlineIngredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
