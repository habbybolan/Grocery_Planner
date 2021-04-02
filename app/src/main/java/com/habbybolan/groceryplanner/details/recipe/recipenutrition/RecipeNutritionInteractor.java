package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeNutritionInteractor {

    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
}
