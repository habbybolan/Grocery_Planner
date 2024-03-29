package com.habbybolan.groceryplanner.listfragments;

import android.content.Context;
import android.view.ActionMode;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Deals with some list functionality of the generic list item T.
 * Deals with the selectMode, and selecting list items.
 * @param <T>   The object type to represent in the list
 */
public abstract class ListFragment<T> extends Fragment implements ListViewInterface<T> {

    // RecyclerView adapter for the ListItems
    protected ListAdapter adapter;
    // actionMode to set up the contextual app bar when a ListItem is selected
    protected ActionMode actionMode = null;
    // the currently selected items inside the ListItems
    protected ArrayList<T> listItemsChecked = new ArrayList<>();
    // The items the list is made up of
    protected List<T> listItems = new ArrayList<>();
    protected ItemListener<T> itemListener;

    private String adapterErrorMessage = "Must attach adapter to ListFragment from child class";
    private String listenerErrorMessage = "Must attach listener to ListFragment with attachListener(Context context)";

    /**
     * Attaches the listener of methods implemented inside Activity.
     * Only use if activity implements the listener of the child fragment.
     */
    public void attachListener(Context context) {
        itemListener = (ItemListener<T>) context;
    }

    /**
     * Called when a list item is selected
     * @param t The list item object selected
     */
    public void onItemSelected(T t) {
        if (actionMode != null) actionMode.finish();
        actionMode = null;
        if (itemListener != null) {
            itemListener.onItemClicked(t);
        } else throw new IllegalArgumentException(listenerErrorMessage);
    }

    @Override
    public void showList(List<T> t) {
        this.listItems.clear();
        this.listItems.addAll(t);
        if (adapter != null) adapter.notifyDataSetChanged();
        else throw new IllegalArgumentException(adapterErrorMessage);
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemCheckBoxSelected(T t) {
        listItemsChecked.add(t);
    }

    @Override
    public void onItemCheckBoxUnSelected(T t) {
        listItemsChecked.remove(t);
        if (listItemsChecked.size() == 0) exitSelectedMode();
    }

    @Override
    public void enterSelectMode() {
        createActionMode();
    }

    @Override
    public void createActionMode() {
        actionMode = getActivity().startActionMode(getActionModeCallback());
    }

    @Override
    public void destroyActionMode() {
        if (actionMode != null) actionMode.finish();
    }

    @Override
    public void exitSelectedMode() {
        destroyActionMode();

        if (adapter != null) adapter.exitSelectedMode();
        else throw new IllegalArgumentException(adapterErrorMessage);
        listItemsChecked.clear();
    }

    public abstract void deleteSelectedItems();
    public abstract ActionMode.Callback getActionModeCallback();

    @Override
    public boolean isSelectMode() {
        if (adapter != null) return adapter.getSelectedMode();
        else throw new IllegalArgumentException();
    }

    public interface ItemListener<T> {

        /**
         * Called when a list item is selected.
         * @param t The list object selected
         */
        void onItemClicked(T t);


    }
}
