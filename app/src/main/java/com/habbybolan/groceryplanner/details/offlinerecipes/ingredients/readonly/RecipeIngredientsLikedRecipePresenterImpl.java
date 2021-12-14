package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;

public class RecipeIngredientsLikedRecipePresenterImpl
        extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.InteractorLikedRecipe, RecipeIngredientsContract.IngredientsView,
        LikedRecipe>
        implements RecipeIngredientsContract.PresenterLikedRecipe {

    public RecipeIngredientsLikedRecipePresenterImpl(RecipeIngredientsContract.InteractorLikedRecipe interactor) {
        super(interactor);
    }
}
