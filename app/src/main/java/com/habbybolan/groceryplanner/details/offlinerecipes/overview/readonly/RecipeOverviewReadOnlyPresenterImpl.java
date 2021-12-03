package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;

/**
 * Skeleton class to facilitate DI
 */
public class RecipeOverviewReadOnlyPresenterImpl
        extends RecipeOverviewPresenterImpl<RecipeOverviewContract.Interactor, RecipeOverviewContract.OverviewView>
        implements RecipeOverviewContract.PresenterReadOnly {

    public RecipeOverviewReadOnlyPresenterImpl(RecipeOverviewContract.Interactor interactor) {
        super(interactor);
    }
}
