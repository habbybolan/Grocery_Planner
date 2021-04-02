package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeNutritionPresenter {

    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    void setView(RecipeNutritionView view);
    void destroy();
}
