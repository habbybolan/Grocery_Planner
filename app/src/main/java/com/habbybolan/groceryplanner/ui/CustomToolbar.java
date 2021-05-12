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

public class CustomToolbar {

    private static final int MAIN_MENU_GROUP_ID = 1;

    private static final int SEARCH_ICON_ID = 0;
    private static final int DELETE_ICON_ID = 1;
    private static final int SORT_ICON_ID = 2;
    private static final int SAVE_ICON_ID = 3;
    private static final int CANCEL_ICON_ID = 4;

    private CustomToolbarBinding toolbarBinding;

    private SearchCallback searchCallback;
    private boolean hasSearchIcon;

    private DeleteCallback deleteCallback;
    private String itemToDelete;
    private boolean hasDeleteIcon;

    private String[] sortMethods;
    private SortCallback sortCallback;
    private boolean hasSortIcon;

    private SaveCallback saveCallback;
    private boolean hasSaveIcon;

    private CancelCallback cancelCallback;
    private boolean hasCancelIcon;

    private int numIcons;
    private String toolbarTitle;

    private TitleSelectCallback titleSelectCallback;
    private String[] titleMethods;
    private boolean canSelectTitle;

    private CustomToolbar() {}

    public static class CustomToolbarBuilder {
        private CustomToolbarBinding toolbarBinding;
        private Context context;
        private LayoutInflater inflater;

        private int numIcons = 0;
        private String toolbarTitle;

        private SearchCallback searchCallback;
        private boolean hasSearchIcon = false;

        private DeleteCallback deleteCallback;
        private String itemToDelete;
        private boolean hasDeleteIcon = false;

        private String[] sortMethods;
        private SortCallback sortCallback;
        private boolean hasSortIcon = false;

        private SaveCallback saveCallback;
        private boolean hasSaveIcon = false;

        private CancelCallback cancelCallback;
        private boolean hasCancelIcon = false;

        private TitleSelectCallback titleSelectCallback;
        private String[] titleMethods;
        private boolean canSelectTitle = false;

        public CustomToolbarBuilder(String toolbarTitle, LayoutInflater inflater, @Nullable ViewGroup container, Context context) {
            this.toolbarTitle = toolbarTitle;
            this.context = context;
            this.inflater = inflater;
            LinearLayout parent = new LinearLayout(inflater.getContext());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbarBinding = DataBindingUtil.inflate(inflater, R.layout.custom_toolbar, container, true);
        }

        public CustomToolbarBuilder addSearch(SearchCallback searchCallback) {
            hasSearchIcon = true;
            this.searchCallback = searchCallback;
            return this;
        }

        public CustomToolbarBuilder addDeleteIcon(DeleteCallback deleteCallback, String itemToDelete) {
            hasDeleteIcon = true;
            this.itemToDelete = itemToDelete;
            this.deleteCallback = deleteCallback;
            return this;
        }

        public CustomToolbarBuilder addSortIcon(SortCallback sortCallback, @SortType.SortListType int sortListType) {
            hasSortIcon = true;
            this.sortCallback = sortCallback;
            this.sortMethods = SortType.getSortListType(sortListType);
            return this;
        }

        public CustomToolbarBuilder addSaveIcon(SaveCallback saveCallback) {
            hasSaveIcon = true;
            this.saveCallback = saveCallback;
            return this;
        }

        public CustomToolbarBuilder addCancelIcon(CancelCallback cancelCallback) {
            hasCancelIcon = true;
            this.cancelCallback = cancelCallback;
            return this;
        }

        public CustomToolbarBuilder allowClickTitle(TitleSelectCallback titleSelectCallback, String[] titleMethods) {
            canSelectTitle = true;
            this.titleSelectCallback = titleSelectCallback;
            this.titleMethods = titleMethods;
            return this;
        }

        public CustomToolbar build() {
            CustomToolbar customToolbar = new CustomToolbar();

            customToolbar.hasSearchIcon = hasSearchIcon;
            customToolbar.searchCallback = searchCallback;
            if (hasSearchIcon) addIconToToolbar(SEARCH_ICON_ID, "Search", android.R.drawable.ic_menu_search);

            customToolbar.hasDeleteIcon = hasDeleteIcon;
            customToolbar.itemToDelete = itemToDelete;
            customToolbar.deleteCallback = deleteCallback;
            if (hasDeleteIcon) addIconToToolbar(DELETE_ICON_ID, "Delete", android.R.drawable.ic_menu_delete);

            customToolbar.sortCallback = sortCallback;
            customToolbar.sortMethods = sortMethods;
            customToolbar.hasSortIcon = hasSortIcon;
            if (hasSortIcon) addIconToToolbar(SORT_ICON_ID, "Sort", android.R.drawable.ic_menu_sort_by_size);

            customToolbar.saveCallback = saveCallback;
            customToolbar.hasSaveIcon = hasSaveIcon;
            if (hasSaveIcon) addIconToToolbar(SAVE_ICON_ID, "Save", android.R.drawable.ic_menu_save);

            customToolbar.cancelCallback = cancelCallback;
            customToolbar.hasCancelIcon = hasCancelIcon;
            if (hasCancelIcon) addIconToToolbar(CANCEL_ICON_ID, "Cancel", android.R.drawable.ic_menu_close_clear_cancel);

            customToolbar.canSelectTitle = canSelectTitle;
            customToolbar.titleSelectCallback = titleSelectCallback;
            customToolbar.titleMethods = titleMethods;

            customToolbar.numIcons = numIcons;

            customToolbar.toolbarTitle = toolbarTitle;
            toolbarBinding.setTitle(toolbarTitle);

            customToolbar.toolbarBinding = toolbarBinding;
            setMenuClickers();

            return customToolbar;
        }

        private void setMenuClickers() {
            Menu menu = toolbarBinding.customToolbar.getMenu();

            if (hasSearchIcon) {
                menu.findItem(SEARCH_ICON_ID).setOnMenuItemClickListener(l -> {
                    searchClicker(menu);
                    return true;
                });
            }
            if (hasDeleteIcon)
                menu.findItem(DELETE_ICON_ID).setOnMenuItemClickListener(l -> {
                    checkDeletePopup();
                    return true;
                });
            if (hasSortIcon)
                menu.findItem(SORT_ICON_ID).setOnMenuItemClickListener(l -> {
                    showSortPopup(toolbarBinding.customToolbar);
                    return true;
                });
            if (hasSaveIcon)
                menu.findItem(SAVE_ICON_ID).setOnMenuItemClickListener(l -> {
                    saveCallback.saveClicked();
                    return true;
                });
            if (hasCancelIcon)
                menu.findItem(CANCEL_ICON_ID).setOnMenuItemClickListener(l -> {
                    cancelCallback.cancelClicked();
                    return true;
                });

            if (canSelectTitle)
                toolbarBinding.toolbarTitle.setOnClickListener(l -> {
                    showTitleSelectPopup(toolbarBinding.toolbarTitle);
                });
        }

        /**
         * On search item clicked, hide menu items showing and toolbar title.
         * Show the editText for searching and a button to close and lookup the search.
         * @param menu  Toolbar menu
         */
        private void searchClicker(Menu menu) {
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
        private void showSortPopup(View v) {
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

        private void showTitleSelectPopup(View v) {
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

        private void checkDeletePopup() {
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
    public interface TitleSelectCallback {
        void selectTitle(int pos);
    }

    public Toolbar getToolbar() {
        return toolbarBinding.customToolbar;
    }
}
