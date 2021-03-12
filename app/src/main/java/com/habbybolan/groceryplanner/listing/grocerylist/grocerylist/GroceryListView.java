package com.habbybolan.groceryplanner.listing.grocerylist.grocerylist;

import com.habbybolan.groceryplanner.models.Grocery;

import java.util.List;

public interface GroceryListView {

    void showGroceryList(List<Grocery> groceries);
    void onGrocerySelected(Grocery grocery);
    void loadingStarted();
    void loadingFailed(String message);
}
