package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionPresenterEditImpl
        extends RecipeNutritionPresenterImpl<RecipeNutritionContract.InteractorEdit, RecipeNutritionContract.NutritionView,
        MyRecipe>
        implements RecipeNutritionContract.PresenterEdit {

    public RecipeNutritionPresenterEditImpl(RecipeNutritionContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void nutritionAmountChanged(Nutrition nutrition) {
        interactor.nutritionAmountChanged(recipe, nutrition);
    }

    @Override
    public void nutritionMeasurementChanged(@NonNull Nutrition nutrition) {
        interactor.nutritionMeasurementChanged(recipe, nutrition);
    }
}
