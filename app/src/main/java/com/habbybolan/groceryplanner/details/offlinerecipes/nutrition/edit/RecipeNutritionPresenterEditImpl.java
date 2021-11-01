package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionPresenterEditImpl extends RecipeNutritionPresenterImpl<RecipeNutritionContract.InteractorEdit, RecipeNutritionContract.NutritionView> implements RecipeNutritionContract.PresenterEdit {



    public RecipeNutritionPresenterEditImpl(RecipeNutritionContract.InteractorEdit interactor) {
        super(interactor);
    }


    @Override
    public void nutritionAmountChanged(OfflineRecipe recipe, Nutrition nutrition) {
        interactor.nutritionAmountChanged(recipe, nutrition);
    }

    @Override
    public void nutritionMeasurementChanged(OfflineRecipe recipe, @NonNull Nutrition nutrition) {
        interactor.nutritionMeasurementChanged(recipe, nutrition);
    }
}
