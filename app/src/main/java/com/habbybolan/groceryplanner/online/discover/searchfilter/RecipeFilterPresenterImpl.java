package com.habbybolan.groceryplanner.online.discover.searchfilter;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeFilterPresenterImpl implements RecipeFilterContract.Presenter{

    private RecipeFilterContract.DiscoverInteractor interactor;
    private boolean isTagsLoading = false;
    private RecipeFilterContract.RecipeFilterView view;

    private WebServiceCallback<RecipeTag> callbackRecipeTags = new WebServiceCallback<RecipeTag>() {
        @Override
        public void onResponse(List<RecipeTag> response) {
            isTagsLoading = false;
            view.showSearchedTagList(response);
        }

        @Override
        public void onFailure(int responseCode, String message) {
            view.loadingFailed(responseCode + ": " + message);
        }
    };

    @Inject
    public RecipeFilterPresenterImpl(RecipeFilterContract.DiscoverInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void addRecipeSearch(String nameSearch, List<OnlineRecipeTag> recipeTags, SortType sortType, RecipeFilterContract.RecipeSearchCallback callback) {
        interactor.addRecipeSearch(nameSearch, recipeTags, sortType, callback);
    }

    @Override
    public void createTagList(String tagSearch) {
        try {
            isTagsLoading = true;
            view.loadingStarted();
            interactor.loadTags(tagSearch, 0, 20, callbackRecipeTags);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            isTagsLoading = false;
        }
    }

    @Override
    public void setView(RecipeFilterContract.RecipeFilterView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public int findLocationOfTagRemovedFromFilter(String title, List<RecipeTag> recipeTags) {
        return interactor.findLocationOfTagRemovedFromFilter(title, recipeTags);
    }

    @Override
    public int removeTag(List<OnlineRecipeTag> recipeTags, String title) {
        return interactor.removeTag(recipeTags, title);
    }
}
