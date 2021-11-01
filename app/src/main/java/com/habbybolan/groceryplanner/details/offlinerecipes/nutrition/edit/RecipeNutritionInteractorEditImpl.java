package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionInteractorEditImpl extends RecipeNutritionInteractorImpl implements RecipeNutritionContract.InteractorEdit {

    public RecipeNutritionInteractorEditImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void nutritionAmountChanged(OfflineRecipe recipe, Nutrition nutrition) {
        // remove the nutrition from the recipe
        if (nutrition.getIsAddedToRecipe() && nutrition.getAmount() == 0) {
            nutrition.setIsAddedToRecipe(false);
            databaseAccess.deleteNutrition(recipe.getId(), Nutrition.getIdFromFrom(nutrition.getName()));
        } else {
            nutrition.setIsAddedToRecipe(true);
            databaseAccess.addNutrition(recipe.getId(), nutrition);
        }
    }

    @Override
    public void nutritionMeasurementChanged(OfflineRecipe recipe, @NonNull Nutrition nutrition) {
        // Can only change measurement type of an existing nutrition
        if (nutrition.getIsAddedToRecipe())
            databaseAccess.addNutrition(recipe.getId(), nutrition);
    }
}
