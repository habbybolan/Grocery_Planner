package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import javax.inject.Inject;

public class DetailsMyRecipePresenterImpl extends RecipeDetailsPresenterImpl<RecipeDetailsContract.InteractorMyRecipe, MyRecipe> implements RecipeDetailsContract.PresenterMyRecipe{

    @Inject
    public DetailsMyRecipePresenterImpl(RecipeDetailsContract.InteractorMyRecipe interactor) {
        super(interactor);
    }

    @Override
    public void onSyncMyRecipe(long recipeId, SyncCompleteCallback callback) {
        interactor.onSyncMyRecipe(recipeId, callback);
    }
}
