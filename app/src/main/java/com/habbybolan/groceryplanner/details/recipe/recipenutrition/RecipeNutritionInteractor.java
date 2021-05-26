package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeNutritionInteractor {

    void updateRecipe(OfflineRecipe offlineRecipe);
    void deleteRecipe(OfflineRecipe offlineRecipe);
}
