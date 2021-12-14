package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public class RecipeIngredientsMyRecipePresenterImpl
        extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.InteractorMyRecipe, RecipeIngredientsContract.IngredientsView,
            MyRecipe>
        implements RecipeIngredientsContract.PresenterMyRecipe {

    public RecipeIngredientsMyRecipePresenterImpl(RecipeIngredientsContract.InteractorMyRecipe interactor) {
        super(interactor);
    }
}
