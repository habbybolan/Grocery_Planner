package com.habbybolan.groceryplanner.online.discover.searchfilter;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFilterInteractorImpl implements RecipeFilterContract.DiscoverInteractor{

    private RestWebService restWebService;

    @Inject
    public RecipeFilterInteractorImpl(RestWebService restWebService) {
        this.restWebService = restWebService;
    }

    @Override
    public void addRecipeSearch(String nameSearch, List<OnlineRecipeTag> recipeTags, SortType sortType, RecipeFilterContract.RecipeSearchCallback callback) {
        if (isValidSearch(nameSearch)) {
            if (recipeTags.size() > 0 && recipeTags.get(0).getIsRecipeSearch()) {
                recipeTags.set(0, new OnlineRecipeTag(nameSearch, true));
                callback.onRecipeSearchReplaced();
            } else {
                recipeTags.add(0, new OnlineRecipeTag(nameSearch, true));
                callback.onRecipeSearchAdded();
            }
        }
        callback.onFail();
    }

    @Override
    public void loadTags(String tagSearch, int offset, int size, WebServiceCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        Call<List<RecipeTag>> call = restWebService.listSearchedRecipeTags(offset, size, SortType.SORT_ALPHABETICAL_ASC_TITLE, tagSearch);
        call.enqueue(new Callback<List<RecipeTag>>() {
            @Override
            public void onResponse(Call<List<RecipeTag>> call, Response<List<RecipeTag>> response) {
                if (response.isSuccessful())
                    callback.onResponse(response.body());
                else
                    callback.onFailure(response.code());
            }

            @Override
            public void onFailure(Call<List<RecipeTag>> call, Throwable t) {
                callback.onFailure(404);
            }
        });
    }

    @Override
    public int findLocationOfTagRemovedFromFilter(String title, List<RecipeTag> recipeTags) {
        for (int i = 0; i < recipeTags.size(); i++) {
            if (recipeTags.get(i).getTitle().equals(title)) return i;
        }
        // title doesn't exist in recipeTags
        return -1;
    }

    @Override
    public int removeTag(List<OnlineRecipeTag> recipeTags, String title) {
        for (int i = 0; i < recipeTags.size(); i++) {
            if (recipeTags.get(i).getTitle().equals(title)) {
                recipeTags.remove(i);
                return i;
            }
        }
        throw new IllegalArgumentException("No tag in filter with the title " + title);
    }

    /**
     * Checks if the String search is valid.
     * @param search    String to search for recipe or tags with.
     * @return          True if the search String is valid
     */
    private boolean isValidSearch(String search) {
        // todo:
        return true;
    }
}
