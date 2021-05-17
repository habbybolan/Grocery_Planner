package com.habbybolan.groceryplanner.online.discover.recipelist;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class OnlineRecipeListInteractorImpl implements OnlineRecipeListContract.Interactor {

    private HttpRecipe httpRecipe;

    @Inject
    public OnlineRecipeListInteractorImpl(HttpRecipe httpRecipe) {
        this.httpRecipe = httpRecipe;
    }

    @Override
    public void loadSearchRecipes(WebServiceCallback<OnlineRecipe> callback, List<OnlineRecipeTag> searchValues, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        // if the recipeSearch exists inside the searchVales, then retrieve it, otherwise set as null.
        String recipeSearch = searchValues.size() > 0 && searchValues.get(0).getIsRecipeSearch() ? searchValues.get(0).getTitle() : null;
        httpRecipe.getRecipes(offset, size, recipeSearch, searchValues, sortType, callback);
    }

    @Override
    public void loadSavedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        httpRecipe.getRecipesSaved(offset, size, userId, sortType, callback);
    }

    @Override
    public void loadUploadedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        httpRecipe.getRecipesUploaded(offset, size, userId, sortType, callback);
    }
}
