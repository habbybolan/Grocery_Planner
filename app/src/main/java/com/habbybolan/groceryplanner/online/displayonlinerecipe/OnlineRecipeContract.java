package com.habbybolan.groceryplanner.online.displayonlinerecipe;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

public interface OnlineRecipeContract {

    interface OnlineRecipeListener {
        OnlineRecipe getOnlineRecipe();
    }
}
