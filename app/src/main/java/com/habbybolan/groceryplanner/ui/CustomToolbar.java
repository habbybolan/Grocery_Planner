package com.habbybolan.groceryplanner.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CustomToolbarBinding;
import com.habbybolan.groceryplanner.databinding.DialogueDeleteBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

/**
 * Facilitates all Toolbars created.
 * Deals with callbacks, design and icons.
 */
public class CustomToolbar {

    private static final int MAIN_MENU_GROUP_ID = 1;

    private enum icons {
        SEARCH_ICON_ID,
        DELETE_ICON_ID,
        SORT_ICON_ID,
        SAVE_ICON_ID,
        CANCEL_ICON_ID,
        SWAP_ICON_ID,
        SYNC_ICON_ID
    }

    private CustomToolbarBinding toolbarBinding;

    private CustomToolbar() {}

    public static class CustomToolbarBuilder {
        private CustomToolbarBinding toolbarBinding;
        private Context context;
        private LayoutInflater inflater;

        private int numIcons = 0;
        CustomToolbar customToolbar;
        Menu menu;

        // TODO: pass context for LayoutInflater instead of actual LayoutInflater. Shouldn't use it directly.
        public CustomToolbarBuilder(String toolbarTitle, LayoutInflater inflater, @Nullable ViewGroup container, Context context) {
            this.context = context;
            this.inflater = inflater;
            LinearLayout parent = new LinearLayout(inflater.getContext());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbarBinding = DataBindingUtil.inflate(inflater, R.layout.custom_toolbar, container, true);

            customToolbar = new CustomToolbar();

            toolbarBinding.setTitle(toolbarTitle);
            customToolbar.toolbarBinding = toolbarBinding;
            menu = toolbarBinding.customToolbar.getMenu();
        }

        public CustomToolbarBuilder addSearch(SearchCallback searchCallback) {
            addIconToToolbar(icons.SEARCH_ICON_ID.ordinal(), "Search", android.R.drawable.ic_menu_search);
            menu.findItem(icons.SEARCH_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                searchClicker(menu, searchCallback);
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addDeleteIcon(DeleteCallback deleteCallback, String itemToDelete) {
            addIconToToolbar(icons.DELETE_ICON_ID.ordinal(), "Delete", android.R.drawable.ic_menu_delete);
            menu.findItem(icons.DELETE_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                checkDeletePopup(deleteCallback, itemToDelete);
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addSortIcon(SortCallback sortCallback, @SortType.SortListType int sortListType) {
            addIconToToolbar(icons.SORT_ICON_ID.ordinal(), "Sort", android.R.drawable.ic_menu_sort_by_size);
            menu.findItem(icons.SORT_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                showSortPopup(toolbarBinding.customToolbar, sortCallback, SortType.getSortListType(sortListType));
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addSaveIcon(SaveCallback saveCallback) {
            addIconToToolbar(icons.SAVE_ICON_ID.ordinal(), "Save", android.R.drawable.ic_menu_save);
            menu.findItem(icons.SAVE_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                saveCallback.saveClicked();
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addCancelIcon(CancelCallback cancelCallback) {
            addIconToToolbar(icons.CANCEL_ICON_ID.ordinal(), "Cancel", android.R.drawable.ic_menu_close_clear_cancel);
            menu.findItem(icons.CANCEL_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                cancelCallback.cancelClicked();
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addSwapIcon(SwapCallback swapCallback) {
            // todo: find a better icon for swap
            addIconToToolbar(icons.SWAP_ICON_ID.ordinal(), "Swap", android.R.drawable.ic_menu_share);
            menu.findItem(icons.SWAP_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                swapCallback.swapClicked();
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder addSyncIcon(SyncCallback syncCallback) {
            addIconToToolbar(icons.SYNC_ICON_ID.ordinal(), "SYnc", android.R.drawable.ic_popup_sync);
            menu.findItem(icons.SYNC_ICON_ID.ordinal()).setOnMenuItemClickListener(l -> {
                syncCallback.syncClicked();
                return true;
            });
            return this;
        }

        public CustomToolbarBuilder allowClickTitle(TitleSelectCallback titleSelectCallback, String[] titleMethods) {
            toolbarBinding.toolbarTitle.setOnClickListener(l -> {
                showTitleSelectPopup(toolbarBinding.toolbarTitle, titleSelectCallback, titleMethods);
            });
            return this;
        }

        public CustomToolbar build() {
            return customToolbar;
        }

        /**
         * On search item clicked, hide menu items showing and toolbar title.
         * Show the editText for searching and a button to close and lookup the search.
         * @param menu  Toolbar menu
         */
        private void searchClicker(Menu menu, SearchCallback searchCallback) {
            toolbarBinding.toolbarTitle.setVisibility(View.GONE);

            animateSearch(View.VISIBLE);

            menu.setGroupVisible(MAIN_MENU_GROUP_ID, false);

            toolbarBinding.btnSearch.setOnClickListener(l -> {
                // set callback with search editText text
                searchCallback.search(toolbarBinding.searchText.getText().toString());
                // reset the search text bar
                toolbarBinding.searchText.setText("");
            });

            toolbarBinding.btnCancel.setOnClickListener(l -> {
                // hide search container
                animateSearch(View.GONE);
                // show menu items
                Transition fadeTrans = new Fade();
                TransitionManager.beginDelayedTransition(toolbarBinding.customToolbar, fadeTrans);
                menu.setGroupVisible(MAIN_MENU_GROUP_ID, true);
                // show title
                toolbarBinding.toolbarTitle.setVisibility(View.VISIBLE);
            });
        }

        private void animateSearch(int visibility) {
            toolbarBinding.searchContainer.setVisibility(visibility);
            if (visibility == View.VISIBLE) {
                // animate search bar
                final Animation searchBarAnim = AnimationUtils.loadAnimation(context, R.anim.anim_scale_view_0_to_100);
                toolbarBinding.searchText.setAnimation(searchBarAnim);
                toolbarBinding.searchText.setVisibility(visibility);

                // animate cancel button
                toolbarBinding.btnCancel.setVisibility(visibility);
                // animate save button
                toolbarBinding.btnSearch.setVisibility(visibility);
            } else {
                toolbarBinding.searchContainer.setVisibility(visibility);
            }
        }

        /**
         * Menu popup for giving different ways to sort the list.
         * @param v     The view to anchor the popup menu to
         */
        private void showSortPopup(View v, SortCallback sortCallback, String[] sortMethods) {
            PopupMenu popup = new PopupMenu(context, v);
            Menu menu = popup.getMenu();
            for (int i = 0; i < sortMethods.length; i++) {
                menu.add(Menu.NONE , i, i, sortMethods[i]);
                int finalI = i;
                menu.findItem(i).setOnMenuItemClickListener(l -> {
                    sortCallback.sortMethodClicked(sortMethods[finalI]);
                    return true;
                });
            }
            popup.show();
        }

        private void showTitleSelectPopup(View v, TitleSelectCallback titleSelectCallback, String[] titleMethods) {
            PopupMenu popup = new PopupMenu(context, v);
            Menu menu = popup.getMenu();
            for (int i = 0; i < titleMethods.length; i++) {
                menu.add(Menu.NONE , i, i, titleMethods[i]);
                int finalI = i;
                menu.findItem(i).setOnMenuItemClickListener(l -> {
                    titleSelectCallback.selectTitle(finalI);
                    return true;
                });
            }
            popup.show();
        }

        private void checkDeletePopup(DeleteCallback deleteCallback, String itemToDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            DialogueDeleteBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialogue_delete, toolbarBinding.customToolbar, false);
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.getRoot())
                    // Add action buttons
                    .setPositiveButton(R.string.btn_delete_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            deleteCallback.deleteClicked();
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel_text, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }});
            binding.setHeaderText("Do you want to delete the " + itemToDelete.toLowerCase() + "?");
            AlertDialog dialog = builder.create();
            dialog.show();

            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
            layoutParams.weight = 10;
            btnPositive.setLayoutParams(layoutParams);
            btnNegative.setLayoutParams(layoutParams);
        }

        /**
         * Adds the menu icon to the toolbar.
         */
        private void addIconToToolbar(int icon_id, String name, int icon_resource) {
            numIcons++;
            Menu menu = toolbarBinding.customToolbar.getMenu();
            menu.add(MAIN_MENU_GROUP_ID, icon_id, icon_id, name);
            MenuItem menuItem = menu.findItem(icon_id);
            if (numIcons <= 3)
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            else
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menuItem.setIcon(icon_resource);
        }
    }

    public interface SearchCallback {
        void search(String search);
    }
    public interface DeleteCallback {
        void deleteClicked();
    }
    public interface SortCallback {
        void sortMethodClicked(String sortMethod);
    }
    public interface SaveCallback {
        void saveClicked();
    }
    public interface CancelCallback {
        void cancelClicked();
    }
    public interface SwapCallback {
        void swapClicked();
    }
    public interface SyncCallback {
        void syncClicked();
    }
    public interface TitleSelectCallback {
        void selectTitle(int pos);
    }

    public Toolbar getToolbar() {
        return toolbarBinding.customToolbar;
    }
}
