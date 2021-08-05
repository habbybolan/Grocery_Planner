package com.habbybolan.groceryplanner.models.primarymodels;

import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.List;

/**
 * Creates a MyRecipe instance to differentiate from LikedRecipe and add functionality to store the user's
 * access level to the recipe.
 */
public class MyRecipe extends OfflineRecipe {

    private AccessLevel accessLevel;

    public MyRecipe(OfflineRecipe offlineRecipe, List<Nutrition> nutritionList, AccessLevel accessLevel) {
        super(offlineRecipe, nutritionList);
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
