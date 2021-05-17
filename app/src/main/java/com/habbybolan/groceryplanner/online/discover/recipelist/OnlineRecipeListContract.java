package com.habbybolan.groceryplanner.online.discover.recipelist;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OnlineRecipeListContract {

    interface Presenter {
        void setView(RecipeListView view);
        void destroy();

        void createSearchList(List<OnlineRecipeTag> searchValues, SortType sortType, int offset, int size);

        void createSavedList(long userId, SortType sortType, int offset, int size);

        void createUploadedList(long userId, SortType sortType, int offset, int size);
    }

    interface Interactor {
        void loadSearchRecipes(WebServiceCallback<OnlineRecipe> callback, List<OnlineRecipeTag> searchValues, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException;

        void loadSavedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException;

        void loadUploadedRecipes(WebServiceCallback<OnlineRecipe> callback, long userId, SortType sortType, int offset, int size) throws ExecutionException, InterruptedException;
    }

    interface RecipeListView {
        void loadingStarted();
        void loadingFailed(String message);

        void showRecipes(List<OnlineRecipe> recipes);
    }

    interface AdapterView {
        void onRecipeClicked(OnlineRecipe onlineRecipe);
    }

    interface OnlineRecipeListListener {
        void onRecipeClicked(OnlineRecipe onlineRecipe);
    }
}
