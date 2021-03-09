package com.habbybolan.groceryplanner.listfragments;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.habbybolan.groceryplanner.R;

/**
 * For list items that are not inside a category
 * Basic functionality of selecting, adding and deleting list items.
 * @param <T>   The list item with no category functionality
 */
public abstract class NonCategoryListFragment<T> extends ListFragment<T> {

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
            inflater.inflate(R.menu.menu_non_category_context, menu);
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
}
