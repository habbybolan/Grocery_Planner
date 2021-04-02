package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.widget.TextView;

import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

public interface PropertyChangedInterface {

    // called when there is a change in the recipe Nutrition
    void onPropertyChanged();

    // Brings up PopupMenu for selecting the measurement type of the nutritional fact
    void onRecipeTypeSelected(Nutrition nutrition, int position, TextView v);
}
