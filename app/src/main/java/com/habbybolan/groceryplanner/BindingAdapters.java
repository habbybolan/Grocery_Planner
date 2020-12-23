package com.habbybolan.groceryplanner;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter(value = {"stepNumber"})
    public static void stepNumber(TextView textView, int stepNumber) {
        String text = stepNumber + ":";
        textView.setText(text);
    }
}
