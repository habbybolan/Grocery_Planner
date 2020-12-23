package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;

public interface RecipeListView {

    void showRecipeList(List<Recipe> recipes);
    void onRecipeSelected(Recipe recipe);
    void loadingStarted();
    void loadingFailed(String message);
}
