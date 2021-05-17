package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

import javax.inject.Inject;

public class OnlineRecipeEditOverviewPresenterImpl implements OnlineRecipeEditOverviewContract.Presenter{

    private OnlineRecipeEditOverviewContract.Interactor interactor;
    private OnlineRecipeEditOverviewContract.OverviewView view;

    @Inject
    public OnlineRecipeEditOverviewPresenterImpl(OnlineRecipeEditOverviewContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void addRecipeTag(String title, List<RecipeTag> recipeTags) {
        if (interactor.addRecipeTag(title, recipeTags)) {
            view.updateTagAdded();
        } else {
            view.loadingFailed("Invalid tag name");
        }
    }

    @Override
    public void setView(OnlineRecipeEditOverviewContract.OverviewView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }
}
