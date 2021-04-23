package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientListInteractorImpl implements IngredientListInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public IngredientListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        // todo:
    }

    @Override
    public void deleteIngredients(List<Ingredient> ingredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredients(ingredientIds);
    }

    @Override
    public void fetchIngredients(DbCallback<Ingredient> callback, SortType sortType) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredients(callback, sortType);
    }

    @Override
    public void searchIngredients(String ingredientName, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchIngredients(ingredientName, callback);
    }
}
