package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSideScrollInteractorImpl implements RecipeSideScrollInteractor{

    private RestWebService restWebService;

    @Inject
    public RecipeSideScrollInteractorImpl(RestWebService restWebService) {
        this.restWebService = restWebService;
    }

    @Override
    public void onRecipeSaved(OnlineRecipe recipe) {
        // todo:
    }

    @Override
    public void fetchRecipes(String infoType, int offset, int amount, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException {
        switch (infoType) {
            case RecipeListType.NEW_TYPE:
                Call<List<OnlineRecipe>> callNew = restWebService.listNewRecipes(offset, amount);
                callNew.enqueue(new Callback<List<OnlineRecipe>>() {
                    @Override
                    public void onResponse(Call<List<OnlineRecipe>> call, Response<List<OnlineRecipe>> response) {
                        if (response.isSuccessful())
                            callback.onResponse(response.body());
                        else callback.onFailure(response.code(), response.message());
                    }

                    @Override
                    public void onFailure(Call<List<OnlineRecipe>> call, Throwable t) {
                        callback.onFailure(404, "");
                    }
                });

                break;
            case RecipeListType.TRENDING_DAY_TYPE:
            case RecipeListType.TRENDING_WEEK_TYPE:
            case RecipeListType.TRENDING_MONTH_TYPE:
            case RecipeListType.TRENDING_YEAR_TYPE:
                Call<List<OnlineRecipe>> callTrending = restWebService.listTrendingRecipes(offset, amount, infoType);
                callTrending.enqueue(new Callback<List<OnlineRecipe>>() {
                    @Override
                    public void onResponse(Call<List<OnlineRecipe>> call, Response<List<OnlineRecipe>> response) {
                        if (response.isSuccessful())
                            callback.onResponse(response.body());
                        else callback.onFailure(response.code(), response.message());
                    }

                    @Override
                    public void onFailure(Call<List<OnlineRecipe>> call, Throwable t) {
                        callback.onFailure(404, "");
                    }
                });
                break;
            default:
                throw new IllegalArgumentException(infoType + " is not a valid type");
        }
    }

}
