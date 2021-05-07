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
    @NonNull
    private String ingredientName;
    private float price;
    @ColumnInfo(name="price_per")
    private int pricePer;
    @ColumnInfo(name="price_type")
    private String priceType;
    @ColumnInfo(name = "food_type")
    private String foodType;

    public IngredientEntity(long ingredientId, @NonNull String ingredientName, float price, int pricePer, String priceType, String foodType) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.foodType = foodType;
    }

    public IngredientEntity(Ingredient ingredient) {
        ingredientId = ingredient.getId();
        ingredientName = ingredient.getName();
        price = ingredient.getPrice();
        pricePer = ingredient.getPricePer();
        priceType = ingredient.getPriceType();
        foodType = ingredient.getFoodType().getType();
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public float getPrice() {
        return price;
    }
    public int getPricePer() {
        return pricePer;
    }
    public String getPriceType() {
        return priceType;
    }
    public String getFoodType() {
        return foodType;
    }
    public long getIngredientId() {
        return ingredientId;
    }
}
