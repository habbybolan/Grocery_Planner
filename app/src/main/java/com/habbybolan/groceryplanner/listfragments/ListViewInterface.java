package com.habbybolan.groceryplanner.listfragments;

import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;

/**
 * Interface for ListFragment to only expose certain methods for children classes to use.
 * @param <T>   Object type to represent in the list
 */
public interface ListViewInterface<T> {

    boolean isSelectMode();
    void enterSelectMode();
    void exitSelectedMode();

    SortType getSortType();

    void createActionMode();
    void destroyActionMode();

    void onItemCheckBoxSelected(T t);
    void onItemCheckBoxUnSelected(T t);

    void onItemSelected(T t);
    void showList(List<T> t);
    void loadingStarted();
    void loadingFailed(String message);
}
