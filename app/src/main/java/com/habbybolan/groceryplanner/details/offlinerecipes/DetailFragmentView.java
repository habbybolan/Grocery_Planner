package com.habbybolan.groceryplanner.details.offlinerecipes;

public interface DetailFragmentView {

    /**
     * Called by activity to update the displayed recipe after sync.
     * Calls database to retrieve the newly updated recipe
     */
    void updateRecipe();
}
