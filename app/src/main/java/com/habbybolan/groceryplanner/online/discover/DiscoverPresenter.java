package com.habbybolan.groceryplanner.online.discover;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;

public interface DiscoverPresenter {

    boolean isValidSearch(String search);

    /**
     * Add the search result to the List of DiscoverRecipeTags.
     * If a recipe search already exists, then replace that one since only one recipe search is allowed in recipeTags.
     * @param text          Recipe search text.
     * @param recipeTags    List of recipe tags searches and at most one recipe search.
     * @param sortType      The order the recipe search should be output.
     */
    void addRecipeSearch(String text, List<DiscoverRecipeTag> recipeTags, SortType sortType);

    /**
     * Add the tag search to the List of DiscoverRecipeTags.
     * @param recipeTag     RecipeTag to add to the search.
     * @param recipeTags    List of recipe tags searches and at most one recipe search.
     * @param sortType      The order the recipe search should be output.
     */
    void addTagSearch(RecipeTag recipeTag, List<DiscoverRecipeTag> recipeTags, SortType sortType);

}
