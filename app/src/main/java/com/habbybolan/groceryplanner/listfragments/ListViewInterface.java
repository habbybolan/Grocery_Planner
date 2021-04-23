package com.habbybolan.groceryplanner.listfragments;

import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;

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
