package com.habbybolan.groceryplanner.online.discover.searchfilter;

import com.habbybolan.groceryplanner.callbacks.WebServiceCallback;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeFilterContract {

    interface Presenter {

        /**
         * Sent the Interactor to update the recipeTags list with the nameSearch.
         * Signals to the view that the list has been updated.
         * @param nameSearch    Recipe search text.
         * @param recipeTags    List of recipe tags searches and at most one recipe search.
         * @param sortType      The order the recipe search should be output.
         * @param callback      callback to describe if the nameSearch was added to the list, or replaced a previous one
         */
        void addRecipeSearch(String nameSearch, List<OnlineRecipeTag> recipeTags, SortType sortType, RecipeSearchCallback callback);

        /**
         * Retrieve the tags from the web service given the tagSearch.
         * @param tagSearch String search for the tag
         */
        void createTagList(String tagSearch);

        void setView(RecipeFilterView view);
        void destroy();

        /**
         * Calls interactor to find the location in the RecipeTags list of the tag that was removed from
         * the filter tags list. Position used to notify the Adapter of the position that was changed.
         * @param title      Title of tag that will be unselected.
         * @param recipeTags List of recipe tags to search through to find position with title value
         * @return      The position of the tag inside RecipeTags, -1 if it doesn't exist in the list.
         */
        int findLocationOfTagRemovedFromFilter(String title, List<RecipeTag> recipeTags);

        /**
         * Calls Interactor to remove the tag with the title inside recipeTags
         * @param recipeTags    List of recipe tags to remove the tag with title
         * @param title         Title of the RecipeTag to remove
         * @return              position of the removed RecipeTag.
         */
        int removeTag(List<OnlineRecipeTag> recipeTags, String title);
    }

    interface RecipeSearchCallback {
        void onRecipeSearchAdded();
        void onRecipeSearchReplaced();
        void onFail();
    }

    interface DiscoverInteractor {

        /**
         * Add the name search to the List of DiscoverRecipeTags.
         * If a recipe search already exists, then replace that one since only one recipe search is allowed in recipeTags.
         * @param nameSearch    Recipe search text.
         * @param recipeTags    List of recipe tags searches and at most one recipe search.
         * @param sortType      The order the recipe search should be output.
         * @param callback      callback to describe if the nameSearch was added to the list, or replaced a previous one
         */
        void addRecipeSearch(String nameSearch, List<OnlineRecipeTag> recipeTags, SortType sortType, RecipeSearchCallback callback);

        void loadTags(String tagSearch, int offset, int size, WebServiceCallback<RecipeTag> callback) throws ExecutionException, InterruptedException;

        /**
         * Finds the location in the RecipeTags list of the tag that was removed from
         * the filter tags list.
         * @param title Title of tag to find
         * @param recipeTags List of recipe tags to search through to find position with title value
         * @return      The position of the tag inside RecipeTags, -1 if it doesn't exist in the list.
         */
        int findLocationOfTagRemovedFromFilter(String title, List<RecipeTag> recipeTags);

        /**
         * Removes the tag with the title inside recipeTags and returns the position it was deleted.
         * @param recipeTags    List of recipe tags to remove the tag with title
         * @param title         Title of the RecipeTag to remove
         * @return              position of the removed RecipeTag.
         */
        int removeTag(List<OnlineRecipeTag> recipeTags, String title);
    }

    interface RecipeFilterView {
        void loadingStarted();
        void loadingFailed(String message);

        void showSearchedTagList(List<RecipeTag> recipeTags);
    }

    interface RecipeTagSearchView {
        void tagFilterAdded(int position);
        void tagFilterRemoved(String title);
    }

}
