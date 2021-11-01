package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeDetailsPresenterImpl<U extends OfflineRecipe> implements RecipeDetailsContract.Presenter<U> {

    protected RecipeDetailsContract.Interactor interactor;
    private RecipeDetailsContract.DetailsView<U> view;

    protected DbSingleCallback<U> callback = new DbSingleCallback<U>() {
        @Override
        public void onResponse(U response) {
            view.showRecipe(response);
        }
    };

    public RecipeDetailsPresenterImpl(RecipeDetailsContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RecipeDetailsContract.DetailsView<U> view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }
}