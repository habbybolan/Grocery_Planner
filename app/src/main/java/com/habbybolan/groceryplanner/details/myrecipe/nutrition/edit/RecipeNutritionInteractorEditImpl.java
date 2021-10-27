package com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public class RecipeNutritionInteractorEditImpl extends RecipeNutritionInteractorImpl implements RecipeNutritionContract.InteractorEdit {

    public RecipeNutritionInteractorEditImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void nutritionAmountChanged(MyRecipe myRecipe, Nutrition nutrition) {
        // remove the nutrition from the recipe
        if (nutrition.getIsAddedToRecipe() && nutrition.getAmount() == 0) {
            nutrition.setIsAddedToRecipe(false);
            databaseAccess.deleteNutrition(myRecipe.getId(), Nutrition.getIdFromFrom(nutrition.getName()));
        } else {
            nutrition.setIsAddedToRecipe(true);
            databaseAccess.addNutrition(myRecipe.getId(), nutrition);
        }
    }

    @Override
    public void nutritionMeasurementChanged(MyRecipe myRecipe, @NonNull Nutrition nutrition) {
        // Can only change measurement type of an existing nutrition
        if (nutrition.getIsAddedToRecipe())
            databaseAccess.addNutrition(myRecipe.getId(), nutrition);
    }
}
