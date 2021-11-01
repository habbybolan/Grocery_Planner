package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class DetailsMyRecipePresenterImpl extends RecipeDetailsPresenterImpl<MyRecipe> implements RecipeDetailsContract.PresenterMyRecipe{

    @Inject
    public DetailsMyRecipePresenterImpl(RecipeDetailsContract.Interactor interactor) {
        super(interactor);
    }

    @Override
    public void loadFullRecipe(long recipeId) {
        try {
            interactor.fetchMyRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
