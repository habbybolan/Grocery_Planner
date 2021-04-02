package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

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
        databaseAccess.deleteGrocery(grocery);
    }

    @Override
    public void deleteGroceries(List<Grocery> groceries) {
        databaseAccess.deleteGroceries(groceries);
    }

    @Override
    public void fetchGroceries(ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceries(groceriesObserver);
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
