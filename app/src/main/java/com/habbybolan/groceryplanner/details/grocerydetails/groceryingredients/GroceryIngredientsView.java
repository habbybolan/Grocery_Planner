package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public interface GroceryIngredientsView {

    void onIngredientSelected(Ingredient ingredient);
    void showIngredientList(List<Ingredient> ingredients);
    void loadingStarted();
    void loadingFailed(String message);
}
