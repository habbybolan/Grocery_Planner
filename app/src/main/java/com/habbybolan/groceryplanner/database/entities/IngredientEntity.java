package com.habbybolan.groceryplanner.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.Ingredient;

@Entity
public class IngredientEntity {
    @NonNull
    @PrimaryKey
    private String ingredientName;
    private int price;
    @ColumnInfo(name="price_per")
    private int pricePer;
    @ColumnInfo(name="price_type")
    private String priceType;
    @ColumnInfo(name = "food_type")
    private String foodType;

    public IngredientEntity(@NonNull String ingredientName, int price, int pricePer, String priceType, String foodType) {
        this.ingredientName = ingredientName;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.foodType = foodType;
    }

    public IngredientEntity(Ingredient ingredient) {
        ingredientName = ingredient.getName();
        price = ingredient.getPrice();
        pricePer = ingredient.getPricePer();
        priceType = ingredient.getPriceType();
        foodType = ingredient.getFoodType().getType();
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public int getPrice() {
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
}
