package com.habbybolan.groceryplanner.listing.grocerylist;

import com.habbybolan.groceryplanner.models.Grocery;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroceryListInteractor {

    /**
     * Delete a grocery
     * @param grocery   grocery to delete
     */
    void deleteGrocery(Grocery grocery);

    /**
     * Delete a list of groceries
     * @param groceries Groceries to delete
     */
    void deleteGroceries(List<Grocery> groceries);
    List<Grocery> fetchGroceries() throws ExecutionException, InterruptedException;
    List<Grocery> searchGroceries(String search);
    void addGrocery(Grocery grocery);
}
