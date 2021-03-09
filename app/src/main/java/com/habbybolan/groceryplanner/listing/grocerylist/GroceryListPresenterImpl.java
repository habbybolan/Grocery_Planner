package com.habbybolan.groceryplanner.listing.grocerylist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.Grocery;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryListPresenterImpl implements GroceryListPresenter {

    // allows talking to GroceryListFragment
    private ListViewInterface view;
    private GroceryListInteractor groceryListInteractor;
    // true if the groceries are being loaded
    private boolean loadingGroceries = false;
    private ObservableArrayList<Grocery> loadedGroceries = new ObservableArrayList<>();

    @Inject
    public GroceryListPresenterImpl(GroceryListInteractor groceryListInteractor) {
        this.groceryListInteractor = groceryListInteractor;
        setGroceryCallback();
    }

    // set up callback for loading Grocery
    private void setGroceryCallback() {
        loadedGroceries.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Grocery>>() {
            @Override
            public void onChanged(ObservableList<Grocery> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<Grocery> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<Grocery> sender, int positionStart, int itemCount) {
                // set the loaded recipe categories as loaded in
                loadingGroceries = false;
                displayGroceries();
            }
            @Override
            public void onItemRangeMoved(ObservableList<Grocery> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<Grocery> sender, int positionStart, int itemCount) {}
        });
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
    public void searchGroceryList(String groceryName) {
        // todo:
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
            groceryListInteractor.fetchGroceries(loadedGroceries);
            if (isViewAttached())
                view.showList(loadedGroceries);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
        }
    }

    private void displayGroceries() {
        if (isViewAttached() && isGroceryReady())
            view.showList(loadedGroceries);
    }
}
