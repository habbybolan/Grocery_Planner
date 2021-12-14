package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddInteractorImpl implements IngredientAddInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public IngredientAddInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchIngredientsNotInIngredientHolder(DbCallback<Ingredient> callback, OfflineIngredientHolder ingredientHolder, String search) throws ExecutionException, InterruptedException {
        if (ingredientHolder.isGrocery())
            if (search.equals(""))
                // get all ingredients not in grocery
                databaseAccess.fetchIngredientsNotInGrocery((Grocery) ingredientHolder, callback);
            else
                // search for ingredients with similar names as search not in grocery
                databaseAccess.fetchIngredientsNotInGrocerySearch((Grocery) ingredientHolder, callback, search);
        else {
            if (search.equals(""))
                // get all ingredients not in recipe
                databaseAccess.fetchIngredientsNotInRecipe((OfflineRecipe) ingredientHolder, callback);
            else
                // search for ingredients with similar names as search not in recipe
                databaseAccess.fetchIngredientsNotInRecipeSearch((OfflineRecipe) ingredientHolder, callback, search);
        }
    }

    @Override
    public void addIngredientToIngredientHolder(String ingredientName, OfflineIngredientHolder ingredientHolder, DbSingleCallbackWithFail<Ingredient> callback) {
        Ingredient ingredient = new Ingredient.IngredientBuilder(ingredientName).build();
        addIngredientToIngredientHolder(ingredient, ingredientHolder, callback);
    }

    @Override
    public void addIngredientToIngredientHolder(Ingredient ingredient, OfflineIngredientHolder ingredientHolder, DbSingleCallbackWithFail<Ingredient> callback) {
        if (ingredientHolder.isGrocery()) {
            databaseAccess.insertUpdateIngredientsIntoGrocery(ingredientHolder.getId(), ingredient, callback);
        } else {
            databaseAccess.insertUpdateIngredientsIntoRecipe(ingredientHolder.getId(), ingredient, callback);
        }
    }
}
