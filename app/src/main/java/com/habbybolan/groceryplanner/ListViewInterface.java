package com.habbybolan.groceryplanner;

import java.util.List;

public interface ListViewInterface<T> {

    boolean isSelectMode();
    void enterSelectMode();
    void exitSelectedMode();

    void createActionMode();
    void destroyActionMode();

    void onItemCheckBoxSelected(T t);
    void onItemCheckBoxUnSelected(T t);

    void onItemSelected(T t);
    void showList(List<T> t);
    void loadingStarted();
    void loadingFailed(String message);
}
