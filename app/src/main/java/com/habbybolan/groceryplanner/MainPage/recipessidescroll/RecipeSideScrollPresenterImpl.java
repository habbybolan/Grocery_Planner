package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeSideScrollPresenterImpl implements RecipeSideScrollPresenter {

    private RecipeSideScrollInteractor interactor;
    private RecipeSideScrollView view;

    private WebServiceCallback<OnlineRecipe> callback = new WebServiceCallback<OnlineRecipe>() {
        @Override
        public void onResponse(List<OnlineRecipe> response) {
            if (isViewAttached()) {
                view.addToList(response);
            }
        }

        @Override
        public void onFailure(int responseCode, String message) {
            view.loadingFailed(responseCode + ": " + message);
        }
    };

    private boolean isViewAttached() {
        return view != null;
    }

    @Inject
    public RecipeSideScrollPresenterImpl(RecipeSideScrollInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(RecipeSideScrollView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void createList(String infoType) {
        try {
            interactor.fetchRecipes(infoType, 0, 20, callback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecipeSaved(OfflineRecipe offlineRecipe) {
        // todo:
    }
}
