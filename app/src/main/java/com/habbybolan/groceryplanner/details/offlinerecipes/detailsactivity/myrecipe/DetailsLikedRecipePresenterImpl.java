package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class DetailsLikedRecipePresenterImpl extends RecipeDetailsPresenterImpl<LikedRecipe> implements RecipeDetailsContract.PresenterLikedRecipe{

    @Inject
    public DetailsLikedRecipePresenterImpl(RecipeDetailsContract.Interactor interactor) {
        super(interactor);
    }

    @Override
    public void loadFullRecipe(long recipeId) {
        try {
            interactor.fetchLikedRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
