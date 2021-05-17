package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.nutrition;

import android.widget.TextView;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface OnlineRecipeNutritionContract {

    interface NutritionListener {
        OnlineRecipe getRecipe();
    }

    interface NutritionAdapterView {
        void onNutritionTypeSelected(TextView view, Nutrition nutrition);
    }
}
