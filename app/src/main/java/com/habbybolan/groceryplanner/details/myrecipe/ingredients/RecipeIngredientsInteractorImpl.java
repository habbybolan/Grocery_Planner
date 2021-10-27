package com.habbybolan.groceryplanner.details.myrecipe.ingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeIngredientsInteractorImpl implements RecipeIngredientsContract.Interactor {

    protected DatabaseAccess databaseAccess;

    @Inject
    public RecipeIngredientsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchIngredients(MyRecipe myRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredientsFromRecipe(myRecipe, sortType, callback);
    }

    @Override
    public void searchIngredients(MyRecipe myRecipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchRecipeIngredients(myRecipe.getId(), ingredientSearch, callback);
    }
}
