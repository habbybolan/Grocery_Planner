package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface RecipeNutritionContract {

    interface Presenter {
        void setView(NutritionView view);
        void destroy();

        /** */
        void nutritionAmountChanged(OfflineRecipe offlineRecipe, Nutrition nutrition);

        void nutritionMeasurementChanged(OfflineRecipe offlineRecipe, @NonNull Nutrition nutrition);
    }

    interface Interactor {

        /**
         * Takes a nutrition List and a specific Nutrition selected, and updates database according to the String value amount
         * @param nutrition         Nutrition value to either add, delete, or update in database
         * @param offlineRecipe     Recipe to change the nutrition values with
         */
        void nutritionAmountChanged(OfflineRecipe offlineRecipe, Nutrition nutrition);

        /**
         * Updated the nutrition value in the database with the altered measurement type.
         * @param offlineRecipe     Recipe's nutrition to update
         * @param nutrition         Nutrition to update to with new measurement type
         */
        void nutritionMeasurementChanged(OfflineRecipe offlineRecipe, @NonNull Nutrition nutrition);

    }

    interface NutritionView {

    }

    interface NutritionChangedListener {

        /**  */
        void nutritionAmountChanged(Nutrition nutrition);

        // Brings up PopupMenu for selecting the measurement type of the nutritional fact
        void onRecipeTypeSelected(@NonNull Nutrition nutrition, TextView v);

        void invalidAction(String message);
    }
}
