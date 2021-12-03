package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryIngredientsInteractorImpl implements GroceryIngredientsInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public GroceryIngredientsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void editGroceryName(Grocery grocery, String name) {
        // todo:
    }

    @Override
    public void deleteGrocery(Grocery grocery) {
        databaseAccess.deleteGrocery(grocery.getId());
    }

    @Override
    public void fetchIngredients(Grocery grocery, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceryIngredients(grocery, callback);
    }

    @Override
    public void deleteIngredient(Grocery grocery, Ingredient ingredient) {
        databaseAccess.deleteIngredientsFromGrocery(grocery.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
    }

    @Override
    public void deleteIngredients(Grocery grocery, List<GroceryIngredient> groceryIngredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (GroceryIngredient ingredient : groceryIngredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredientsFromGrocery(grocery.getId(), ingredientIds);
    }

    @Override
    public void updateGroceryIngredientSelected(Grocery grocery, GroceryIngredient groceryIngredient) {
        databaseAccess.updateGroceryIngredientChecked(grocery, groceryIngredient);
    }

    @Override
    public void searchIngredients(Grocery grocery, String ingredientSearch, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchGroceryIngredients(grocery.getId(), ingredientSearch, callback);
    }
}
