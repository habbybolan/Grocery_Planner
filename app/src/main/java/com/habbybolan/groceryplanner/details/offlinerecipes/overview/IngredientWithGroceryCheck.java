package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

public class IngredientWithGroceryCheck extends Ingredient {

    // true if the recipe ingredient is being added to the grocery list
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
