package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

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
    void fetchGroceries(DbCallback<Grocery> callback, SortType sortType) throws ExecutionException, InterruptedException;
    /**
     * Find Grocery items based on the groceryName typed.
     * @param grocerySearch   The string to search in grocery names
     * @param callback        Callback for retrieving list of groceries
     */
    void searchGroceries(String grocerySearch, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException;
    void addGrocery(Grocery grocery);
}
