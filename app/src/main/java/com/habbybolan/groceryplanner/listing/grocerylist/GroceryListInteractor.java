package com.habbybolan.groceryplanner.listing.grocerylist;

import com.habbybolan.groceryplanner.models.Grocery;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroceryListInteractor {

    List<Grocery> fetchGroceries() throws ExecutionException, InterruptedException;
    List<Grocery> searchGroceries(String search);
    void addGrocery(Grocery grocery);
}
