package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe;

import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

import javax.inject.Inject;

public class DetailsLikedRecipePresenterImpl extends RecipeDetailsPresenterImpl<RecipeDetailsContract.InteractorLikedRecipe, LikedRecipe> implements RecipeDetailsContract.PresenterLikedRecipe{

    @Inject
    public DetailsLikedRecipePresenterImpl(RecipeDetailsContract.InteractorLikedRecipe interactor) {
        super(interactor);
    }
}
