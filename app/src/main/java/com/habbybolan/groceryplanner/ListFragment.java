package com.habbybolan.groceryplanner;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public abstract class ListFragment<T> extends Fragment implements ListViewInterface<T> {

    // RecyclerView adapter for the ListItems
    protected ListAdapter adapter;
    // actionMode to set up the contextual app bar when a ListItem is selected
    private ActionMode actionMode = null;
    // the currently selected items inside the ListItems
    protected ArrayList<T> listItemsChecked = new ArrayList<>();
    // The items the list is made up of
    protected List<T> listItems = new ArrayList<>();
    private ItemListener<T> itemListener;

    private String adapterErrorMessage = "Must attach adapter to ListFragment from child class)";
    private String listenerErrorMessage = "Must attach listener to ListFragment with attachListener(Context context)";

    public void attachListener(Context context) {
        itemListener = (ItemListener<T>) context;
    }

    public void onItemSelected(T t) {
        if (actionMode != null) actionMode.finish();
        actionMode = null;
        if (itemListener != null) {
            itemListener.showToolbar();
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
    public void createActionMode() {
        actionMode = getActivity().startActionMode(actionModeCallback);
    }

    @Override
    public void destroyActionMode() {
        if (actionMode != null) actionMode.finish();
    }

    @Override
    public void enterSelectMode() {
        if (itemListener != null) itemListener.hideToolbar();
        else throw new IllegalArgumentException(listenerErrorMessage);
        createActionMode();
    }

    @Override
    public void exitSelectedMode() {
        destroyActionMode();
        if (itemListener != null) itemListener.showToolbar();
        else throw new IllegalArgumentException(listenerErrorMessage);

        if (adapter != null) adapter.exitSelectedMode();
        else throw new IllegalArgumentException(adapterErrorMessage);
        listItemsChecked.clear();
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_ingredient_context, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_ingredient_context_delete:
                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                    deleteSelectedItems();
                    exitSelectedMode();
                    return true;
                case R.id.action_ingredient_context_cancel:
                    exitSelectedMode();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            exitSelectedMode();
        }
    };

    public abstract void deleteSelectedItems();

    @Override
    public boolean isSelectMode() {
        if (adapter != null) return adapter.getSelectedMode();
        else throw new IllegalArgumentException();
    }

    public interface ItemListener<T> {

        void onItemClicked(T t);
        void hideToolbar();
        void showToolbar();
    }
}
