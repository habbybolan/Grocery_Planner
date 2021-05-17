package com.habbybolan.groceryplanner.http.requests;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface HttpRecipe {

    void getRecipesNew(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipesTrending(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipesSaved(int offset, int numRows, long userId, SortType sortType, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipesUploaded(int offset, int numRows, long userId, SortType sortType, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getRecipes(int offset, int numRows, String recipeSearch, List<OnlineRecipeTag> tagSearches, SortType sortType, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException;
    void getTags(int offset, int size, String search, WebServiceCallback<RecipeTag> callback) throws ExecutionException, InterruptedException;
}
