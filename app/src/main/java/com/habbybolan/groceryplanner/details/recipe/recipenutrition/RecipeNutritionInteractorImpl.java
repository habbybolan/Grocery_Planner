package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionInteractorImpl implements RecipeNutritionContract.Interactor {

    private DatabaseAccess databaseAccess;

    public RecipeNutritionInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void nutritionAmountChanged(OfflineRecipe offlineRecipe, Nutrition nutrition) {
        // remove the nutrition from the recipe
        if (nutrition.getIsAddedToRecipe() && nutrition.getAmount() == 0) {
            nutrition.setIsAddedToRecipe(false);
            databaseAccess.deleteNutrition(offlineRecipe.getId(), Nutrition.getIdFromFrom(nutrition.getName()));
        } else {
            nutrition.setIsAddedToRecipe(true);
            databaseAccess.addNutrition(offlineRecipe.getId(), nutrition);
        }
    }

    @Override
    public void nutritionMeasurementChanged(OfflineRecipe offlineRecipe, @NonNull Nutrition nutrition) {
        // Can only change measurement type of an existing nutrition
        if (nutrition.getIsAddedToRecipe())
            databaseAccess.addNutrition(offlineRecipe.getId(), nutrition);
    }
}
