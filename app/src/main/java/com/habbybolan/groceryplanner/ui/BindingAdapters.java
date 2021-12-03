package com.habbybolan.groceryplanner.ui;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/**
 * Binding adapter methods used in layout XML files.
 */
public class BindingAdapters {

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
     * Adapter for displaying quantity information of an Ingredient.
     * @param textView      The view to display the quantity String
     * @param quantity      The quantity of the Ingredient
     * @param quantityType  The type of the quantity of the Ingredient
     */
    @BindingAdapter(value = {"quantity", "quantityType"}, requireAll = false)
    public static void ingredientQuantityDisplay(TextView textView, Float quantity, String quantityType) {
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

    /**
     * Adapter for displaying quantity information of an ingredient with the ingredient name
     * @param textView      The view to display the quantity String
     * @param quantity      The quantity of the Ingredient
     * @param quantityType  The type of the quantity of the Ingredient
     */
    @BindingAdapter(value = {"quantity", "quantityType", "ingredientName"})
    public static void ingredientQuantityDisplayWithName(TextView textView, Float quantity, String quantityType, String ingredientName) {
        String s = quantity + " " + quantityType + " " + ingredientName;
        textView.setText(s);
    }

    /**
     * Adapter for displaying the Groceries that contain Recipes in a list.
     * @param textView      [groceryName]:  [recipeAmount]
     * @param groceryName   Name of the grocery
     * @param recipeAmount  amount recipe is added to the Ingredient
     */
    @BindingAdapter(value = {"groceryName", "recipeAmount"})
    public static void GroceriesHoldingIngredientDisplay(TextView textView, String groceryName, int recipeAmount) {
        String text = groceryName + ":  " + recipeAmount;
        textView.setText(text);
    }
}
