package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

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
    public void deleteGrocery(Grocery grocery) {
        databaseAccess.deleteGrocery(grocery.getId());
    }

    @Override
    public void deleteGroceries(List<Grocery> groceries) {
        List<Long> groceryIds = new ArrayList<>();
        for (Grocery grocery : groceries) groceryIds.add(grocery.getId());
        databaseAccess.deleteGroceries(groceryIds);
    }

    @Override
    public void fetchGroceries(DbCallback<Grocery> callback, SortType sortType) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceries(callback, sortType);
    }

    @Override
    public void searchGroceries(String grocerySearch, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchGroceries(grocerySearch, callback);
    }

    @Override
    public void addGrocery(Grocery grocery) {
        databaseAccess.addGrocery(grocery);
    }
}
