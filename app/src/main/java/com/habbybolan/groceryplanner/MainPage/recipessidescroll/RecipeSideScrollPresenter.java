package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

public interface RecipeSideScrollPresenter {

    void attachView(RecipeSideScrollView view);
    void destroy();

    /**
     * Retrieve the data from the RESTFul api, the type based on the recipeType.
     * @param infoType    Defines the type of info to gather. ex) New recipes, Trending recipes.
     */
    void createList(int infoType);

    /**
     * On button clicked to save recipe, store the saved recipe on online database.
     * @param recipe    Recipe to save to online database.
     */
    void onRecipeSaved(Recipe recipe);
}
