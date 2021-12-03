package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionPresenterImpl;

/**
 * Skeleton class to facilitate DI
 */
public class RecipeNutritionPresenterReadOnlyImpl extends RecipeNutritionPresenterImpl<RecipeNutritionContract.Interactor, RecipeNutritionContract.NutritionView> implements RecipeNutritionContract.PresenterReadOnly {

    public RecipeNutritionPresenterReadOnlyImpl(RecipeNutritionContract.Interactor interactor) {
        super(interactor);
    }
}
