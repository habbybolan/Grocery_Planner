package com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly;

import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsPresenterImpl;

public class RecipeIngredientsReadOnlyPresenterImpl extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.Interactor, RecipeIngredientsContract.IngredientsView> implements RecipeIngredientsContract.PresenterReadOnly {

    public RecipeIngredientsReadOnlyPresenterImpl(RecipeIngredientsContract.Interactor interactor) {
        super(interactor);
    }
}
