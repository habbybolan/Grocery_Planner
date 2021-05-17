package com.habbybolan.groceryplanner.online.discover.searchfilter;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeFilterInteractorImpl implements RecipeFilterContract.DiscoverInteractor{

    private HttpRecipe httpRecipe;

    @Inject
    public RecipeFilterInteractorImpl(HttpRecipe httpRecipe) {
        this.httpRecipe = httpRecipe;
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
       httpRecipe.getTags(offset, size, tagSearch, callback);
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
