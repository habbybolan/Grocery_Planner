package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionPresenterImpl implements RecipeNutritionContract.Presenter {

    RecipeNutritionContract.Interactor interactor;
    RecipeNutritionContract.NutritionView view;

    public RecipeNutritionPresenterImpl(RecipeNutritionContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RecipeNutritionContract.NutritionView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void nutritionAmountChanged(OfflineRecipe offlineRecipe, Nutrition nutrition) {
        interactor.nutritionAmountChanged(offlineRecipe, nutrition);
    }

    @Override
    public void nutritionMeasurementChanged(OfflineRecipe offlineRecipe, @NonNull Nutrition nutrition) {
        interactor.nutritionMeasurementChanged(offlineRecipe, nutrition);
    }
}
