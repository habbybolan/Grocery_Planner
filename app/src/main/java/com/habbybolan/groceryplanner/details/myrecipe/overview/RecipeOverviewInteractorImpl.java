package com.habbybolan.groceryplanner.details.myrecipe.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

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

    @Override
    public void fetchGroceriesHoldingRecipe(MyRecipe myRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesHoldingRecipe(myRecipe, callback);
    }



    @Override
    public void fetchTags(MyRecipe myRecipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeTags(myRecipe.getId(), callback);
    }

}
