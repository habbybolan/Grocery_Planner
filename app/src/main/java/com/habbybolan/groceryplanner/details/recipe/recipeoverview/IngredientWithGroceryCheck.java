package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

public class IngredientWithGroceryCheck extends Ingredient {

    private boolean isInGrocery;

    public IngredientWithGroceryCheck(Ingredient ingredient, boolean isInGrocery) {
        super(ingredient);
        this.isInGrocery = isInGrocery;
    }

    public boolean getIsInGrocery() {
        return isInGrocery;
    }
    public void setIsInGrocery(boolean isCheck) {
        this.isInGrocery = isCheck;
    }
}
