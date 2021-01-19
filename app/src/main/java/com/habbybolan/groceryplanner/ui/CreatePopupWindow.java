package com.habbybolan.groceryplanner.ui;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.PopupDeleteBinding;

public class CreatePopupWindow {

    /**
     * Creates a popupWindow to checking for user to confirm on deleting object from a list.
     * @param viewGroup         The viewGroup to anchor the popupWindow to
     * @param objectToDelete    The string of the object to delete
     * @return                  The view that can be clicked to confirm the delete
     */
    public static View createPopupDeleteCheck(ViewGroup viewGroup, String objectToDelete, final PopupWindow popupWindow) {
        PopupDeleteBinding popupDeleteBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.popup_delete, null, false);
        popupDeleteBinding.setObjectToDelete(objectToDelete);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow.setContentView(popupDeleteBinding.getRoot());
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(viewGroup, Gravity.CENTER, 0, 0);
        // return the clickable view
        return popupDeleteBinding.popupBtnDelete;
    }
}
