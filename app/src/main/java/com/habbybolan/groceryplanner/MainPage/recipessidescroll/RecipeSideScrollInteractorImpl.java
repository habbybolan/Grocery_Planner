package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeSideScrollInteractorImpl implements RecipeSideScrollInteractor{

    private HttpRecipe httpRecipe;

    @Inject
    public RecipeSideScrollInteractorImpl(HttpRecipe httpRecipe) {
        this.httpRecipe = httpRecipe;
    }

    @Override
    public void onRecipeSaved(OnlineRecipe recipe) {
        // todo:
    }

    @Override
    public void fetchRecipes(int infoType, int offset, int amount, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException {
        switch (infoType) {
            case RecipeListType.NEW_TYPE:
                httpRecipe.getRecipesNew(offset, amount, callback);
                break;
            case RecipeListType.TRENDING_TYPE:
                httpRecipe.getRecipesTrending(offset, amount, callback);
                break;
            default:
                throw new IllegalArgumentException(infoType + " is not a valid type");
        }
    }

}
