package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.concurrent.ExecutionException;

public class RecipeOverviewInteractorImpl implements RecipeOverviewContract.Interactor {

    protected DatabaseAccess databaseAccess;

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }
}
