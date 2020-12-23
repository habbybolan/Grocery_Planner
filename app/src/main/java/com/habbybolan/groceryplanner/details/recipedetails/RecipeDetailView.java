package com.habbybolan.groceryplanner.details.recipedetails;

import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public interface RecipeDetailView {

    void onIngredientSelected(Ingredient ingredient);
    void showIngredientList(List<Ingredient> ingredients);
    void loadingStarted();
    void loadingFailed(String message);
}
