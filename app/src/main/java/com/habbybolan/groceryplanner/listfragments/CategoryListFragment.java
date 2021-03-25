package com.habbybolan.groceryplanner.listfragments;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.models.Category;

import java.util.ArrayList;

/**
 * For list items that are put inside a category.
 * Includes basic functionality for adding and deleting list items with context menu.
 * Adds functionality for putting the list item inside a category
 * @param <T>   List item that can be put in a category
 */
public abstract class CategoryListFragment<T> extends ListFragment<T> {

    @Override
    public ActionMode.Callback getActionModeCallback() {
        return actionModeCallback;
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_category_context, menu);
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
                case R.id.action_category_context_delete:
                    deleteSelectedItems();
                    exitSelectedMode();
                    return true;
                case R.id.action_category_context_add_category:
                    categoryPopup(getActivity().findViewById(R.id.action_category_context_add_category));
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

    protected abstract void addSelectedItemsToCategory(Category category);
    protected abstract void removeSelectedItemsFromCategory();
    protected abstract ArrayList<? extends Category> getCategories();

    /**
     * Creates Popup menu window to add selected items to selected category.
     */
    private void categoryPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        Menu menu = popup.getMenu();
        ArrayList<? extends Category> categories = getCategories();
        // first menu item removes the recipe from any category
        menu.add("No Category");
        menu.getItem(0).setOnMenuItemClickListener(l -> {
            removeSelectedItemsFromCategory();
            exitSelectedMode();
            return true;
        });
        // menu items that add the selected recipes to the specific Category
        for (int i = 1; i < categories.size()+1; i++) {
            Category category = categories.get(i-1);
            menu.add(category.getName());
            menu.getItem(i).setOnMenuItemClickListener(c -> {
                addSelectedItemsToCategory(category);
                exitSelectedMode();
                return true;
            });
        }

        popup.show();
    }
}
