package com.habbybolan.groceryplanner.details.myrecipe.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class RecipeDetailsPresenterImpl implements RecipeDetailsContract.Presenter {

    private RecipeDetailsContract.Interactor interactor;
    private RecipeDetailsContract.DetailsView view;

    private DbSingleCallback<MyRecipe> callback = new DbSingleCallback<MyRecipe>() {
        @Override
        public void onResponse(MyRecipe response) {
            view.showRecipe(response);
        }
    };

    @Inject
    public RecipeDetailsPresenterImpl(RecipeDetailsContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(RecipeDetailsContract.DetailsView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void loadFullRecipe(long recipeId) {
        try {
            interactor.fetchRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
