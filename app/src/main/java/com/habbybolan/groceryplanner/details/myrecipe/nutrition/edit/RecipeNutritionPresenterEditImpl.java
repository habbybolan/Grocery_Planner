package com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionPresenterEditImpl extends RecipeNutritionPresenterImpl<RecipeNutritionContract.InteractorEdit, RecipeNutritionContract.NutritionView> implements RecipeNutritionContract.PresenterEdit {



    public RecipeNutritionPresenterEditImpl(RecipeNutritionContract.InteractorEdit interactor) {
        super(interactor);
    }


    @Override
    public void nutritionAmountChanged(MyRecipe myRecipe, Nutrition nutrition) {
        interactor.nutritionAmountChanged(myRecipe, nutrition);
    }

    @Override
    public void nutritionMeasurementChanged(MyRecipe myRecipe, @NonNull Nutrition nutrition) {
        interactor.nutritionMeasurementChanged(myRecipe, nutrition);
    }
}
