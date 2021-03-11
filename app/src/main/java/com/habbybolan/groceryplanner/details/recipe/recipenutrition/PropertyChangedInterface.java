package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.widget.TextView;

import com.habbybolan.groceryplanner.models.Nutrition;

public interface PropertyChangedInterface {

    // called when there is a change in the recipe Nutrition
    void onPropertyChanged();

    void onRecipeTypeSelected(Nutrition nutrition, int position, TextView v);
}
