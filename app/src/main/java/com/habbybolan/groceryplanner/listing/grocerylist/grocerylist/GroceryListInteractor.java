package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

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
    void fetchGroceries(ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException;
    List<Grocery> searchGroceries(String search);
    void addGrocery(Grocery grocery);
}
