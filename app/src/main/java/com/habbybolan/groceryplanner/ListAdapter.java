package com.habbybolan.groceryplanner;

import android.view.View;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListAdapter<T extends RecyclerView.ViewHolder, E> extends RecyclerView.Adapter<T> {

    protected boolean selectMode = false;
    protected ListViewInterface view;
    protected List<E> items;

    public ListAdapter(ListViewInterface view, List<E> items) {
        this.view = view;
        this.items = items;
    }

    protected void exitSelectedMode() {
        if (selectMode) {
            selectMode = false;
            notifyDataSetChanged();
        }
    }

    protected boolean getSelectedMode() {
        return selectMode;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Functionality for clicking a checkbox, either selecting or un-selecting it
     * @param checkBox  The checkbox to be selected/unselected
     * @param position  The position of the item with the checkBox clicked
     */
    protected void checkBoxClick(CheckBox checkBox, int position) {
        if (checkBox.isChecked())
            view.onItemCheckBoxSelected(items.get(position));
        else
            view.onItemCheckBoxUnSelected(items.get(position));
    }

    /**
     * Functionality for clicking on a list Item
     * @param checkBox  The CheckBox view of the list item selected
     * @param position  The position of the List Item selected
     */
    protected void setOnCLickItem(CheckBox checkBox, int position) {
        // if in the selectMode, only select its the item's CheckBox
        if (selectMode)
            checkBoxClick(checkBox, position);
        else {
            // otherwise, allow normal clicking of item
            view.onItemSelected(items.get(position));
        }
    }

    /**
     * Functionality for clicking on an List Item's CheckBox
     * @param checkBox  The CheckBox view that was selected
     * @param position  The position on the item with the selected CheckBox
     */
    protected void setOnCLickItemCheckBox(CheckBox checkBox, int position) {
        checkBoxClick(checkBox, position);
    }

    /**
     * Functionality for long clicking a List Item
     * @param position  Position of the list item long clicked
     * @return          True if not already in select mode, otherwise false
     */
    protected boolean setOnLongCLickItem(int position) {
        // todo: make the long-clicked item selected
        if (!selectMode) {
            selectMode = true;
            view.enterSelectMode();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }


    protected void displayCheckBox(CheckBox checkBox) {
        // if the select mode is activated, then display checkboxes to select multiple items
        if (selectMode) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
        }
    }
}

