package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

public class RecipeNutritionLikedRecipePresenterImpl
        extends RecipeNutritionPresenterImpl<RecipeNutritionContract.InteractorLikedRecipeReadOnly, RecipeNutritionContract.NutritionView, LikedRecipe>
        implements RecipeNutritionContract.PresenterLikedRecipe{

    public RecipeNutritionLikedRecipePresenterImpl(RecipeNutritionContract.InteractorLikedRecipeReadOnly interactor) {
        super(interactor);
    }
}
