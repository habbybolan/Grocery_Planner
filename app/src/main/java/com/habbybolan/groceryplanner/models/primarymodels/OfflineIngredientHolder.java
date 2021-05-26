package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcelable;

public interface OfflineIngredientHolder extends Parcelable {
    String OFFLINE_INGREDIENT_HOLDER = "offline_ingredient_holder";

    long getId();
    boolean isGrocery();
}
