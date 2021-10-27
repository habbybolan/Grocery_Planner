package com.habbybolan.groceryplanner.details.myrecipe.nutrition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface RecipeNutritionContract {

    interface Presenter<T extends NutritionView> {
        void setView(T view);
        void destroy();
    }

    interface PresenterEdit extends Presenter<NutritionView> {
        void nutritionAmountChanged(MyRecipe myRecipe, Nutrition nutrition);

        void nutritionMeasurementChanged(MyRecipe myRecipe, @NonNull Nutrition nutrition);
    }

    interface PresenterReadOnly extends Presenter<NutritionView> {}

    interface Interactor {
        // If needed in future
    }

    interface InteractorEdit extends Interactor {

        /**
         * Takes a nutrition List and a specific Nutrition selected, and updates database according to the String value amount
         * @param nutrition         Nutrition value to either add, delete, or update in database
         * @param myRecipe     Recipe to change the nutrition values with
         */
        void nutritionAmountChanged(MyRecipe myRecipe, Nutrition nutrition);

        /**
         * Updated the nutrition value in the database with the altered measurement type.
         * @param myRecipe     Recipe's nutrition to update
         * @param nutrition         Nutrition to update to with new measurement type
         */
        void nutritionMeasurementChanged(MyRecipe myRecipe, @NonNull Nutrition nutrition);
    }

    interface NutritionView {
        // If needed in future
    }

    interface NutritionChangedListener {

        /**  */
        void nutritionAmountChanged(Nutrition nutrition);

        // Brings up PopupMenu for selecting the measurement type of the nutritional fact
        void onRecipeTypeSelected(@NonNull Nutrition nutrition, TextView v);

        void invalidAction(String message);
    }

    interface RecipeNutritionListener {
        MyRecipe getMyRecipe();
        void onSwapViewNutrition();
    }
}
