package com.habbybolan.groceryplanner.online.discover.recipelist;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineRecipeListInteractorImpl implements OnlineRecipeListContract.Interactor {

    private RestWebService restWebService;

    @Inject
    public OnlineRecipeListInteractorImpl(RestWebService restWebService) {
        this.restWebService = restWebService;
    }

    @Override
    public void loadSearchRecipes(WebServiceCallback<OnlineRecipe> callback, List<OnlineRecipeTag> searchValues, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        /*// if the recipeSearch exists inside the searchVales, then retrieve it, otherwise set as null.
        String recipeSearch = searchValues.size() > 0 && searchValues.get(0).getIsRecipeSearch() ? searchValues.get(0).getTitle() : null;
        httpRecipe.getRecipes(offset, size, recipeSearch, searchValues, sortType, callback);*/

        // get the name search if it exists
        String nameSearch = "";
        if (searchValues.size() > 0 && searchValues.get(0).getIsRecipeSearch()) {
            nameSearch = searchValues.get(0).getTitle();
        }
        // get the list of tag searches if they exist
        List<String> tagSearches = new ArrayList<>();
        for (OnlineRecipeTag onlineRecipeTag : searchValues) {
            if (!onlineRecipeTag.getIsRecipeSearch()) tagSearches.add(onlineRecipeTag.getTitle());
        }

        Call<List<OnlineRecipe>> call = restWebService.listSearchedRecipes(offset, size, sortType.getSortTitle(), nameSearch, tagSearches);
        call.enqueue(new Callback<List<OnlineRecipe>>() {
            @Override
            public void onResponse(Call<List<OnlineRecipe>> call, Response<List<OnlineRecipe>> response) {
                if (response.isSuccessful())
                    callback.onResponse(response.body());
                else
                    callback.onFailure(response.code());
            }

            @Override
            public void onFailure(Call<List<OnlineRecipe>> call, Throwable t) {
                callback.onFailure(404);
            }
        });

    }

    @Override
    public void loadSavedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        Call<List<OnlineRecipe>> call = restWebService.listSavedRecipes(userId, offset, size, sortType.getSortTitle());
        call.enqueue(new Callback<List<OnlineRecipe>>() {
            @Override
            public void onResponse(Call<List<OnlineRecipe>> call, Response<List<OnlineRecipe>> response) {
                if (response.isSuccessful())
                    callback.onResponse(response.body());
                else
                    callback.onFailure(response.code());
            }

            @Override
            public void onFailure(Call<List<OnlineRecipe>> call, Throwable t) {
                callback.onFailure(404);
            }
        });
    }

    @Override
    public void loadUploadedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException {
        Call<List<OnlineRecipe>> call = restWebService.listUploadedRecipes(userId, offset, size, sortType.getSortTitle());
        call.enqueue(new Callback<List<OnlineRecipe>>() {
            @Override
            public void onResponse(Call<List<OnlineRecipe>> call, Response<List<OnlineRecipe>> response) {
                if (response.isSuccessful())
                    callback.onResponse(response.body());
                else
                    callback.onFailure(response.code());
            }

            @Override
            public void onFailure(Call<List<OnlineRecipe>> call, Throwable t) {
                callback.onFailure(404);
            }
        });
    }
}
