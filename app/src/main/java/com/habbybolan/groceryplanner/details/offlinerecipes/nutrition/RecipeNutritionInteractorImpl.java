package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

import com.habbybolan.groceryplanner.database.DatabaseAccess;

public class RecipeNutritionInteractorImpl implements RecipeNutritionContract.Interactor {

    protected DatabaseAccess databaseAccess;

    public RecipeNutritionInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
}
