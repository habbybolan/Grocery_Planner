package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.concurrent.ExecutionException;

public abstract class RecipeOverviewInteractorImpl<T extends OfflineRecipe> implements RecipeOverviewContract.Interactor<T> {

    protected DatabaseAccess databaseAccess;

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }
}
