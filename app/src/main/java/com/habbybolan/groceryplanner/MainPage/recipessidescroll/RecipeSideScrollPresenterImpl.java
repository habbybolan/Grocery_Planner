package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeSideScrollPresenterImpl implements RecipeSideScrollPresenter {

    private RecipeSideScrollInteractor interactor;
    private RecipeSideScrollView view;

    private WebServiceCallback<OnlineRecipe> callback = new WebServiceCallback<OnlineRecipe>() {
        @Override
        public void onResponse(List<OnlineRecipe> response, int responseCode) {
            if (isViewAttached()) {
                if (responseCode == 200)
                    view.addToList(response);
                else
                    view.loadingFailed(responseCode + " error when loading recipes");
            }
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
    public void createList(int infoType) {
        try {
            interactor.fetchRecipes(infoType, 0, 20, callback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecipeSaved(Recipe recipe) {
        // todo:
    }
}
