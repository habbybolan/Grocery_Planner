package com.habbybolan.groceryplanner;

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
    protected void checkBoxClick(CheckBox checkBox, int position, List items) {
        if (checkBox.isChecked())
            view.onItemCheckBoxSelected(items.get(position));
        else
            view.onItemCheckBoxUnSelected(items.get(position));
    }
}

