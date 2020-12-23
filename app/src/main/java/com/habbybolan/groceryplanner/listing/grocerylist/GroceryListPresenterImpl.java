package com.habbybolan.groceryplanner.listing.grocerylist;

import com.habbybolan.groceryplanner.models.Grocery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryListPresenterImpl implements GroceryListPresenter {

    // allows talking to GroceryListFragment
    private GroceryListView view;
    private GroceryListInteractor groceryListInteractor;
    private List<Grocery> loadedGroceries = new ArrayList<>();

    @Inject
    public GroceryListPresenterImpl(GroceryListInteractor groceryListInteractor) {
        this.groceryListInteractor = groceryListInteractor;
    }

    /**
     * Creates the grocery list from saved database. If successful, calls 'view' to signal groceryListFragment
     * that the list is created and ready to display.s
     */
    @Override
    public void createGroceryList() {
        try {
            view.loadingStarted();
            loadedGroceries = groceryListInteractor.fetchGroceries();
            if (isViewAttached())
                view.showGroceryList(loadedGroceries);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
        }
    }

    /**
     * Check if view is attached to communicate with fragment.
     * @return  True if the GroceryListFragment view interface is added to presenter
     */
    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void setView(GroceryListView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void searchGroceryList(String groceryName) {
        // todo:
    }

    @Override
    public void addGrocery(Grocery grocery) {
        groceryListInteractor.addGrocery(grocery);
        try {
            loadedGroceries = groceryListInteractor.fetchGroceries();
            if (isViewAttached())
                view.showGroceryList(loadedGroceries);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve datra");
        }
    }
}
