package com.habbybolan.groceryplanner.details.myrecipe.overview.readonly;

import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewPresenterImpl;

public class RecipeOverviewReadOnlyPresenterImpl extends RecipeOverviewPresenterImpl<RecipeOverviewContract.Interactor, RecipeOverviewContract.OverviewView>  implements RecipeOverviewContract.PresenterReadOnly {

    public RecipeOverviewReadOnlyPresenterImpl(RecipeOverviewContract.Interactor interactor) {
        super(interactor);
    }
}
