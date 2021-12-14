package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeNutritionMyRecipePresenterImpl
        extends RecipeNutritionPresenterImpl<RecipeNutritionContract.InteractorMyRecipeReadOnly, RecipeNutritionContract.NutritionView, MyRecipe>
        implements RecipeNutritionContract.PresenterMyRecipe {

    public RecipeNutritionMyRecipePresenterImpl(RecipeNutritionContract.InteractorMyRecipeReadOnly interactor) {
        super(interactor);
    }
}
