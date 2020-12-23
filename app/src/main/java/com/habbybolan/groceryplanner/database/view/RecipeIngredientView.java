package com.habbybolan.groceryplanner.database.view;


import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

/**
 * Allows inner join of Ingredient on the RecipeIngredientBridge to associate the Recipe id's to an Ingredients.
 */

@DatabaseView("SELECT *" +
        " FROM RecipeIngredientBridge" +
        " INNER JOIN IngredientEntity ON RecipeIngredientBridge.ingredientName = IngredientEntity.ingredientName")
public class RecipeIngredientView {

    // Ingredient
    private String ingredientName;
    private int price;
    @ColumnInfo(name = "price_per")
    private int pricePer;
    @ColumnInfo(name = "price_type")
    private String priceType;

    // Bridge
    private long recipeId;
    private int quantity;
    @ColumnInfo(name = "quantity_type")
    private String quantityType;

    public RecipeIngredientView(String ingredientName, int price, int pricePer, String priceType, long recipeId, int quantity, String quantityType) {
        this.ingredientName = ingredientName;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.recipeId = recipeId;
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
    public long getRecipeId() {
        return recipeId;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getQuantityType() {
        return quantityType;
    }
}
