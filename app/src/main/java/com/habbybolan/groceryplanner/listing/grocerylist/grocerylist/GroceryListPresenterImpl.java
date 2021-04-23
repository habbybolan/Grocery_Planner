package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryListPresenterImpl implements GroceryListPresenter {

    // allows talking to GroceryListFragment
    private ListViewInterface view;
    private GroceryListInteractor groceryListInteractor;
    // true if the groceries are being loaded
    private boolean loadingGroceries = false;
    private List<Grocery> loadedGroceries = new ArrayList<>();

    private DbCallback<Grocery> groceryDbCallback = new DbCallback<Grocery>() {
        @Override
        public void onResponse(List<Grocery> response) {
            // set the loaded recipe categories as loaded in
            loadedGroceries.clear();
            loadedGroceries.addAll(response);
            loadingGroceries = false;
            displayGroceries();
        }
    };

    @Inject
    public GroceryListPresenterImpl(GroceryListInteractor groceryListInteractor) {
        this.groceryListInteractor = groceryListInteractor;
    }

    private boolean isGroceryReady() {
        return !loadingGroceries;
    }

    /**
     * Check if view is attached to communicate with fragment.
     * @return  True if the GroceryListFragment view interface is added to presenter
     */
    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteGrocery(Grocery grocery) {
        groceryListInteractor.deleteGrocery(grocery);
        createGroceryList();
    }

    @Override
    public void deleteGroceries(List<Grocery> groceries) {
        groceryListInteractor.deleteGroceries(groceries);
        createGroceryList();
    }

    @Override
    public void searchGroceryList(String grocerySearch) {
        try {
            loadingGroceries = true;
            view.loadingStarted();
            groceryListInteractor.searchGroceries(grocerySearch, groceryDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingGroceries = false;
        }
    }

    @Override
    public void addGrocery(Grocery grocery) {
        groceryListInteractor.addGrocery(grocery);
        createGroceryList();
    }

    /**
     * Creates the grocery list from saved database. If successful, calls 'view' to signal groceryListFragment
     * that the list is created and ready to display.s
     */
    @Override
    public void createGroceryList() {
        try {
            loadingGroceries = true;
            view.loadingStarted();
            groceryListInteractor.fetchGroceries(groceryDbCallback, view.getSortType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingGroceries = false;
        }
    }

    private void displayGroceries() {
        if (isViewAttached() && isGroceryReady())
            view.showList(loadedGroceries);
    }
}
