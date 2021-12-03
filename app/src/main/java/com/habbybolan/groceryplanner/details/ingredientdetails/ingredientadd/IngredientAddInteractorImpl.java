package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

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
    public void fetchIngredientsNotInIngredientHolder(DbCallback<Ingredient> callback, OfflineIngredientHolder ingredientHolder) throws ExecutionException, InterruptedException {
        if (ingredientHolder.isGrocery())
            databaseAccess.fetchIngredientsNotInGrocery((Grocery) ingredientHolder, callback);
        else
            databaseAccess.fetchIngredientsNotInRecipe((OfflineRecipe) ingredientHolder, callback);
    }

    @Override
    public void addCheckedToIngredientHolder(HashSet<Ingredient> checkedIngredients, OfflineIngredientHolder ingredientHolder) {
        List<Ingredient> checkIngredientsArray = new ArrayList<>(checkedIngredients);
        if (ingredientHolder.isGrocery()) {
            databaseAccess.insertIngredientsIntoGrocery(ingredientHolder.getId(), checkIngredientsArray);
        } else {
            databaseAccess.insertIngredientsIntoRecipe(ingredientHolder.getId(), checkIngredientsArray);
        }
    }
}
