package com.habbybolan.groceryplanner.models.primarymodels;

/**
 * Creates an LikedRecipe instance of OfflineRecipe to distinguish from MyRecipe.
 */
public class LikedRecipe extends OfflineRecipe {

    public LikedRecipe(OfflineRecipe offlineRecipe) {
        super(offlineRecipe);
    }
}
