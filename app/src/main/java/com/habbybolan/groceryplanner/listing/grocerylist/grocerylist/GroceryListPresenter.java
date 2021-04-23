package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import java.util.List;

public interface GroceryListPresenter {

    /**
     * Creates view inside presenter to allow methods to be called from Presenter implemented inside GroceryListFragment.
     * Calls method to set up the beginning of the grocery list.
     * @param view  Implementation holding methods implemented in GroceryListFragment.
     */
    void setView(ListViewInterface view);

    /**
     * Create a list of groceries from database
     * Call for the list to be displayed if the view is connected.
     */
    void createGroceryList();

    /**
     * Destroy the Presenter.
     */
    void destroy();

    /**
     * Delete a grocery
     * @param grocery   grocery to delete
     */
    void deleteGrocery(Grocery grocery);

    /**
     * Delete a list of groceries
     * @param groceries Groceries to delete
     */
    void deleteGroceries(List<Grocery>groceries);

    /**
     * Find Grocery items based on the groceryName typed.
     * @param grocerySearch   The string to search in grocery names
     */
    void searchGroceryList(String grocerySearch);

    /**
     * Add a grocery object to the database and display it.
     * @param grocery   The Grocery object to be added to the list.
     */
    void addGrocery(Grocery grocery);


}
