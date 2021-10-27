package com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly;

import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionPresenterImpl;

public class RecipeNutritionPresenterReadOnlyImpl extends RecipeNutritionPresenterImpl<RecipeNutritionContract.Interactor, RecipeNutritionContract.NutritionView> implements RecipeNutritionContract.PresenterReadOnly {

    public RecipeNutritionPresenterReadOnlyImpl(RecipeNutritionContract.Interactor interactor) {
        super(interactor);
    }
}
