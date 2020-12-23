package com.habbybolan.groceryplanner.listing.grocerylist;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Grocery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryListInteractorImpl implements GroceryListInteractor {
    private static String TAG = "GroceryListInteractorImpl";

    private DatabaseAccess databaseAccess;

    @Inject
    public GroceryListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public List<Grocery> fetchGroceries() throws ExecutionException, InterruptedException {
        return databaseAccess.fetchGroceries();
    }

    @Override
    public ArrayList<Grocery> searchGroceries(String search) {
        // todo:
        return null;
    }

    @Override
    public void addGrocery(Grocery grocery) {
        databaseAccess.addGrocery(grocery);
    }
}
