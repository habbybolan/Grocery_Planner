package com.habbybolan.groceryplanner.MainPage.recipesnippet;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import javax.inject.Inject;

public class RecipeSnippetPresenterImpl implements RecipeSnippetPresenter {

    private RecipeSnippetInteractor interactor;

    @Inject
    public RecipeSnippetPresenterImpl(RecipeSnippetInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setView(RecipeSnippetView view) {

    }

    @Override
    public void onSaveRecipeToOnline(OnlineRecipe recipe) {

    }

    @Override
    public void onSaveRecipeOffline(OnlineRecipe recipe) {

    }
}
