package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import com.google.gson.JsonElement;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.lists.MyRecipeList;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.sync.SyncRecipeFromResponse;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsMyRecipeInteractorImpl implements RecipeDetailsContract.InteractorMyRecipe {

    private RestWebService restWebService;
    private SyncRecipeFromResponse syncRecipes;
    private DatabaseAccess databaseAccess;

    @Inject
    public RecipeDetailsMyRecipeInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService, SyncRecipeFromResponse syncRecipes) {
        this.syncRecipes = syncRecipes;
        this.databaseAccess = databaseAccess;
        this.restWebService = restWebService;
    }

    @Override
    public void fetchMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullMyRecipe(recipeId, callback);
    }

    @Override
    public void onSyncMyRecipe(long recipeId, SyncCompleteCallback callback) {
        try {
            databaseAccess.fetchFullMyRecipe(recipeId, new DbSingleCallback<MyRecipe>() {
                @Override
                public void onResponse(MyRecipe response) {
                    MyRecipeList list = new MyRecipeList.MyRecipeListBuilder().addRecipe(response).build();
                    Call<JsonElement> call = restWebService.syncMyRecipes(list);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            syncRecipes.syncMyRecipes(response.body().getAsJsonArray(), callback);
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            callback.onSyncFailed(t.getMessage());
                        }
                    });
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
