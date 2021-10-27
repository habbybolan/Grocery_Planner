package com.habbybolan.groceryplanner.models.primarymodels;

/**
 * Creates a MyRecipe instance to differentiate from LikedRecipe and add functionality to store the user's
 * access level to the recipe.
 */
public class MyRecipe extends OfflineRecipe {

    private AccessLevel accessLevel;

    public MyRecipe(OfflineRecipe offlineRecipe, AccessLevel accessLevel) {
        super(offlineRecipe);
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}
