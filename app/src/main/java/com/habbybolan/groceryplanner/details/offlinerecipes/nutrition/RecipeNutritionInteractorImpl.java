package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeNutritionInteractorImpl<T extends OfflineRecipe> implements RecipeNutritionContract.Interactor<T> {

    protected DatabaseAccess databaseAccess;

    public RecipeNutritionInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
}
