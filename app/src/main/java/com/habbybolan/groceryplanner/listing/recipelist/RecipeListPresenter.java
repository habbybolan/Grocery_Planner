package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.Recipe;

public interface RecipeListPresenter {

    void destroy();
    void addRecipe(Recipe recipe);
    void setView(RecipeListView view);
    void createRecipeList();

}
