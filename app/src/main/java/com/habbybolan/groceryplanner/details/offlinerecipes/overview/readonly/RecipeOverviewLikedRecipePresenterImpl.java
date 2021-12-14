package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

public class RecipeOverviewLikedRecipePresenterImpl
        extends RecipeOverviewPresenterImpl<RecipeOverviewContract.InteractorLikedRecipeReadOnly, RecipeOverviewContract.OverviewView, LikedRecipe>
        implements RecipeOverviewContract.PresenterLikedRecipe {

    public RecipeOverviewLikedRecipePresenterImpl(RecipeOverviewContract.InteractorLikedRecipeReadOnly interactor) {
        super(interactor);
    }
}
