package com.habbybolan.groceryplanner.online.discover.recipelist;

import com.habbybolan.groceryplanner.callbacks.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class OnlineRecipeListPresenterImpl implements OnlineRecipeListContract.Presenter {

    private OnlineRecipeListContract.Interactor interactor;
    private OnlineRecipeListContract.RecipeListView view;

    private boolean isLoadingRecipes = false;
    private WebServiceCallback<OnlineRecipe> callback = new WebServiceCallback<OnlineRecipe>() {
        @Override
        public void onResponse(List<OnlineRecipe> response) {
            isLoadingRecipes = false;
            view.showRecipes(response);
        }

        @Override
        public void onFailure(int responseCode, String message) {
            isLoadingRecipes = false;
            view.loadingFailed(responseCode + ": " + message);
        }
    };

    @Inject
    public OnlineRecipeListPresenterImpl(OnlineRecipeListContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(OnlineRecipeListContract.RecipeListView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void createSearchList(List<OnlineRecipeTag> searchValues, SortType sortType, int offset, int size) {
        try {
            isLoadingRecipes = true;
            view.loadingStarted();
            interactor.loadSearchRecipes(callback, searchValues, sortType, offset, size);
        } catch (ExecutionException | InterruptedException e) {
            isLoadingRecipes = false;
            view.loadingFailed("failed to retrieve recipes");
        }
    }

    @Override
    public void createSavedList(long userId, SortType sortType, int offset, int size) {
        try {
            isLoadingRecipes = true;
            view.loadingStarted();
            interactor.loadSavedRecipes(callback, userId, sortType, offset, size);
        } catch (ExecutionException | InterruptedException e) {
            isLoadingRecipes = false;
            view.loadingFailed("failed to retrieve recipes");
        }
    }

    @Override
    public void createUploadedList(long userId, SortType sortType, int offset, int size) {
        try {
            isLoadingRecipes = true;
            view.loadingStarted();
            interactor.loadUploadedRecipes(callback, userId, sortType, offset, size);
        } catch (ExecutionException | InterruptedException e) {
            isLoadingRecipes = false;
            view.loadingFailed("failed to retrieve recipes");
        }
    }
}
