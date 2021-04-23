package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddInteractorImpl implements IngredientAddInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public IngredientAddInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchIngredientsNotInIngredientHolder(DbCallback<Ingredient> callback, IngredientHolder ingredientHolder) throws ExecutionException, InterruptedException {
        if (ingredientHolder.isGrocery())
            databaseAccess.fetchIngredientsNotInGrocery((Grocery) ingredientHolder, callback);
        else
            databaseAccess.fetchIngredientsNotInRecipe((Recipe) ingredientHolder, callback);
    }

    @Override
    public void addCheckedToIngredientHolder(HashSet<Ingredient> checkedIngredients, IngredientHolder ingredientHolder) {
        List<Ingredient> checkIngredientsArray = new ArrayList<>(checkedIngredients);
        databaseAccess.addIngredients(ingredientHolder, checkIngredientsArray);
    }
}
