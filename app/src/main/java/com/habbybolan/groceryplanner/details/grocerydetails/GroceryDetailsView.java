package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public interface GroceryDetailsView {

    void onIngredientSelected(Ingredient ingredient);
    void showIngredientList(List<Ingredient> ingredients);
    void loadingStarted();
    void loadingFailed(String message);
}
