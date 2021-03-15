package com.habbybolan.groceryplanner.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.DialogueDeleteBinding;

public class PopupBuilder {

    public static void createDeleteDialogue(LayoutInflater inflater, String objectToDelete, ViewGroup viewGroup, Context context, DeleteDialogueInterface deleteDialogueInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogueDeleteBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialogue_delete, viewGroup, false);
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(binding.getRoot())
                // Add action buttons
                .setPositiveButton(R.string.btn_delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDialogueInterface.deleteItem();
                    }
                })
                .setNegativeButton(R.string.btn_cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }});
        binding.setHeaderText("Do you want to delete the " + objectToDelete.toLowerCase() + "?");
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    public interface DeleteDialogueInterface {
        void deleteItem();
    }
}
