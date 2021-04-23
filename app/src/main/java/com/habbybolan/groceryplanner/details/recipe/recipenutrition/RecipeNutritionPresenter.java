package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractor;

public interface RecipeNutritionPresenter extends RecipeDetailsInteractor {
    
    void setView(RecipeNutritionView view);
    void destroy();
}
