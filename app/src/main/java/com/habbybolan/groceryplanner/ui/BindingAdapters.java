package com.habbybolan.groceryplanner.ui;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    /**
     * Adapter for displaying the step number of the String recipe step.
     * @param textView      The view to display the step
     * @param stepNumber    The position of the step in the list of steps
     */
    @BindingAdapter(value = {"stepNumber"})
    public static void stepNumber(TextView textView, int stepNumber) {
        String text = stepNumber + ":";
        textView.setText(text);
    }

    /**
     * Adapter for displaying the object to delete text inside the delete popup.
     * @param textView          The view to hold the text
     * @param objectToDelete    The name of the item to delete
     */
    @BindingAdapter(value = {"objectToDelete"})
    public static void objectToDelete(TextView textView, String objectToDelete) {
        String text = "Do you want to delete the " + objectToDelete + "?";
        textView.setText(text);
    }

    /**
     * Adapter for displaying price information of an Ingredient displayed in an ingredient list.
     * @param textView  View to place the price information in
     * @param price     The price of the ingredient
     * @param pricePer  The amount of the ingredient at you can get at price
     * @param priceType The type of pricePer
     */
    @BindingAdapter(value = {"price", "pricePer", "priceType"}, requireAll = false)
    public static void ingredientPriceDisplay(TextView textView, Integer price, Integer pricePer, String priceType) {
        StringBuilder builder = new StringBuilder();
        if (price != null) {
            builder.append(" At ");
            builder.append("$"); // tie this to a setting to set the type of currency
            builder.append(price);
            if (pricePer != null) {
                builder.append("/");
                builder.append(pricePer);
                if (priceType != null) {
                    builder.append(priceType);
                }
            }
        }
        String text = builder.toString();
        textView.setText(text);
    }

    /**
     * Adapter for displaying quantity information of an Ingredient.
     * @param textView      The view to display the quantity String
     * @param quantity      The quantity of the Ingredient
     * @param quantityType  The type of the quantity of the Ingredient
     */
    @BindingAdapter(value = {"quantity", "quantityType"}, requireAll = false)
    public static void ingredientQuantityDisplay(TextView textView, Integer quantity, String quantityType) {
        StringBuilder builder = new StringBuilder();
        if (quantity != null) {
            builder.append(quantity);
            if (quantityType != null) {
                builder.append(quantityType);
            }
        }
        String text = builder.toString();
        textView.setText(text);
    }
}
