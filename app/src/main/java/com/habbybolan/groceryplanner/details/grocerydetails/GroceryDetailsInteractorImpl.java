package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.database.DatabaseAccess;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryDetailsInteractorImpl implements GroceryDetailsInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public GroceryDetailsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void editGroceryName(Grocery grocery, String name) {
        // todo:
    }

    @Override
    public void deleteGrocery(Grocery grocery) {
        databaseAccess.deleteGrocery(grocery);
    }

    @Override
    public List<Ingredient> fetchIngredients(Grocery grocery) throws ExecutionException, InterruptedException {
        return databaseAccess.fetchIngredientsFromGrocery(grocery);
    }
}
