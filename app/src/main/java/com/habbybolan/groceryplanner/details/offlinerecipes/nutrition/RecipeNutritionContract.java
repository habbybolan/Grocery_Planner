package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.details.offlinerecipes.DetailFragmentView;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface RecipeNutritionContract {

    interface Presenter<U extends Interactor<T2>, T extends NutritionView, T2 extends OfflineRecipe> {
        void setupPresenter(T view, long recipeId);
        void destroy();

        T2 getRecipe();

        void loadUpdatedRecipe();
    }

    interface PresenterEdit extends Presenter<InteractorEdit, NutritionView, MyRecipe> {
        void nutritionAmountChanged(Nutrition nutrition);

        void nutritionMeasurementChanged(@NonNull Nutrition nutrition);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly<T extends Interactor<U>, U extends OfflineRecipe> extends Presenter<T, NutritionView, U> {}

    interface PresenterMyRecipe extends PresenterReadOnly<InteractorMyRecipeReadOnly, MyRecipe> {}

    interface PresenterLikedRecipe extends PresenterReadOnly<InteractorLikedRecipeReadOnly, LikedRecipe> {}

    interface Interactor<T extends OfflineRecipe> {

        void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<T> callback);
    }

    interface InteractorEdit extends Interactor<MyRecipe> {

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

    interface InteractorMyRecipeReadOnly extends Interactor<MyRecipe> {}

    interface InteractorLikedRecipeReadOnly extends Interactor<LikedRecipe> {}

    interface NutritionView extends DetailFragmentView {
        /** Display the updated recipe specific values. */
        void displayUpdatedRecipe();

        /** setup the recipe views. */
        void setupRecipeViews();
    }

    interface NutritionEditView extends NutritionView {
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
    }

    interface RecipeNutritionMyRecipeListener extends RecipeNutritionListener {
        void onSwapViewNutrition();
        void onSync();
    }

    interface RecipeNutritionLikedRecipeListener extends RecipeNutritionListener {
    }
}
