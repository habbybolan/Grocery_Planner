package com.habbybolan.groceryplanner.http.requests;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.concurrent.ExecutionException;

public interface HttpRecipe {

    void getRecipesNew(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipesTrending(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipesSaved(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
}
