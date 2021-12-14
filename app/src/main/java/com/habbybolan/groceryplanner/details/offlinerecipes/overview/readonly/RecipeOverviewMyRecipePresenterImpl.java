package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeOverviewMyRecipePresenterImpl
        extends RecipeOverviewPresenterImpl<RecipeOverviewContract.InteractorMyRecipeReadOnly,
        RecipeOverviewContract.OverviewView, MyRecipe>
        implements RecipeOverviewContract.PresenterMyRecipe {


    public RecipeOverviewMyRecipePresenterImpl(RecipeOverviewContract.InteractorMyRecipeReadOnly interactor) {
        super(interactor);
    }
}
