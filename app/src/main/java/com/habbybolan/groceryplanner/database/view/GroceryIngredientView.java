package com.habbybolan.groceryplanner.database.view;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

/**
 * Allows inner join of Ingredient on the GroceryIngredientBridge to associate the Grocery id's to an Ingredients.
 */
@DatabaseView("SELECT *" +
        " FROM GroceryIngredientBridge" +
        " INNER JOIN IngredientEntity ON GroceryIngredientBridge.ingredientName = IngredientEntity.ingredientName")
public class GroceryIngredientView {

    // Ingredient
    private String ingredientName;
    private int price;
    @ColumnInfo(name = "price_per")
    private int pricePer;
    @ColumnInfo(name = "price_type")
    private String priceType;

    // Bridge
    private long groceryId;
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;
    private int quantity;
    @ColumnInfo(name = "quantity_type")
    private String quantityType;

    public GroceryIngredientView(String ingredientName, int price, int pricePer, String priceType, long groceryId, boolean isFavorite, int quantity, String quantityType) {
        this.ingredientName = ingredientName;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.groceryId = groceryId;
        this.isFavorite = isFavorite;
        this.quantity = quantity;
        this.quantityType = quantityType;
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
    public long getGroceryId() {
        return groceryId;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getQuantityType() {
        return quantityType;
    }

}
