package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface RecipeNutritionContract {

    interface Presenter<T extends NutritionView> {
        void setView(T view);
        void destroy();
    }

    interface PresenterEdit extends Presenter<NutritionView> {
        void nutritionAmountChanged(OfflineRecipe recipe, Nutrition nutrition);

        void nutritionMeasurementChanged(OfflineRecipe recipe, @NonNull Nutrition nutrition);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly extends Presenter<NutritionView> {}

    interface Interactor {
        // If needed in future
    }

    interface InteractorEdit extends Interactor {

        /**
         * Takes a nutrition List and a specific Nutrition selected, and updates database according to the String value amount
         * @param nutrition         Nutrition value to either add, delete, or update in database
         * @param recipe     Recipe to change the nutrition values with
         */
        void nutritionAmountChanged(OfflineRecipe recipe, Nutrition nutrition);

        /**
         * Updated the nutrition value in the database with the altered measurement type.
         * @param recipe     Recipe's nutrition to update
         * @param nutrition         Nutrition to update to with new measurement type
         */
        void nutritionMeasurementChanged(OfflineRecipe recipe, @NonNull Nutrition nutrition);
    }

    interface NutritionView {
        // If needed in future
    }

    interface NutritionEditView extends NutritionView{
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
        OfflineRecipe getRecipe();
    }

    interface RecipeNutritionMyRecipeListener extends RecipeNutritionListener {
        void onSwapViewNutrition();
        void onSync();
    }

    interface RecipeNutritionLikedRecipeListener extends RecipeNutritionListener {
    }
}
