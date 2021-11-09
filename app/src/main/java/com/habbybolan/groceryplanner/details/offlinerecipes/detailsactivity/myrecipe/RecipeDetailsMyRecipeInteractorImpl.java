package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.lists.MyRecipeList;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsMyRecipeInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeDetailsContract.InteractorMyRecipe {

    public RecipeDetailsMyRecipeInteractorImpl(DatabaseAccess databaseAccess, RestWebService restWebService) {
        super(databaseAccess, restWebService);
    }

    @Override
    public void fetchMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchFullMyRecipe(recipeId, callback);
    }

    @Override
    public void onSync(MyRecipe myRecipe) {
        MyRecipeList list = new MyRecipeList.MyRecipeListBuilder().addRecipe(myRecipe).build();
        Call<JsonObject> call = restWebService.syncedMyRecipes(list);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // todo:
                System.out.println("test");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // todo:
                System.out.println("test");
            }
        });
    }
}
