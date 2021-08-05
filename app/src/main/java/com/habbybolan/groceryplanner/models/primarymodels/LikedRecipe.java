package com.habbybolan.groceryplanner.models.primarymodels;

import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

/**
 * Creates an LikedRecipe instance of OfflineRecipe to distinguish from MyRecipe.
 */
public class LikedRecipe extends OfflineRecipe {

    public LikedRecipe(OfflineRecipe offlineRecipe, List<Nutrition> nutritionList) {
        super(offlineRecipe, nutritionList);
    }
}
