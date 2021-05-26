package com.habbybolan.groceryplanner.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

@Entity(indices = {@Index(value = {"ingredientName"}, unique = true)})
public class IngredientEntity {

    @PrimaryKey(autoGenerate = true)
    private long ingredientId;
    private Long onlineIngredientId;
    @NonNull
    private String ingredientName;
    @ColumnInfo(name = "food_type")
    private String foodType;

    public IngredientEntity(long ingredientId, Long onlineIngredientId, @NonNull String ingredientName, String foodType) {
        this.ingredientId = ingredientId;
        this.onlineIngredientId = onlineIngredientId;
        this.ingredientName = ingredientName;
        this.foodType = foodType;
    }

    public IngredientEntity(Ingredient ingredient) {
        ingredientId = ingredient.getId();
        onlineIngredientId = ingredient.getOnlineId();
        ingredientName = ingredient.getName();
        foodType = ingredient.getFoodType().getType();
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public String getFoodType() {
        return foodType;
    }
    public long getIngredientId() {
        return ingredientId;
    }
    public Long getOnlineIngredientId() {
        return onlineIngredientId;
    }
}
