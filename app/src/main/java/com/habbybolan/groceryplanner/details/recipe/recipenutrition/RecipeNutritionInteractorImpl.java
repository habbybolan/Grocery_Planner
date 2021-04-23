package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractorImpl;

public class RecipeNutritionInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeNutritionInteractor {
    
    public RecipeNutritionInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }
}
