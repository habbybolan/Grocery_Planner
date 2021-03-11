package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Recipe;

public class RecipeNutritionInteractorImpl implements RecipeNutritionInteractor {

    private DatabaseAccess databaseAccess;
    public RecipeNutritionInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
    @Override
    public void updateRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe);
    }
}
