package com.habbybolan.groceryplanner.models.ingredientmodels;

public class RecipeWithIngredient {

    private int ingredientQuantity;
    private String ingredientQuantityType;
    private long recipeId;
    private String recipeName;
    private int recipeAmount;
    private String foodType;

    public RecipeWithIngredient(long recipeId, String recipeName, int recipeAmount, int ingredientQuantity,
                                String ingredientQuantityType) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeAmount = recipeAmount;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientQuantityType = ingredientQuantityType;
    }

    public int getIngredientQuantity() {
        return ingredientQuantity;
    }
    public String getIngredientQuantityType() {
        return ingredientQuantityType;
    }
    public long getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public int getRecipeAmount() {
        return recipeAmount;
    }
}
