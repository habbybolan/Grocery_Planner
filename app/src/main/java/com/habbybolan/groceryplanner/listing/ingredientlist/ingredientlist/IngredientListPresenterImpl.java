package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientListPresenterImpl implements IngredientListPresenter {

    private IngredientListInteractor interactor;
    private ListViewInterface view;

    // ingredients loaded from the database
    private List<Ingredient> loadedIngredients = new ArrayList<>();
    // true if the ingredients are being retrieved from the database
    private boolean loadingIngredients = false;

    private DbCallback<Ingredient> ingredientDbCallback = new DbCallback<Ingredient>() {
        @Override
        public void onResponse(List<Ingredient> response) {
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            loadingIngredients = false;
            view.showList(loadedIngredients);
        }
    };


    @Inject
    public IngredientListPresenterImpl(IngredientListInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    @Override
    public void createIngredientList() {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            interactor.fetchIngredients(ingredientDbCallback, view.getSortType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
        }
    }

    @Override
    public void destroy() {
        this.view = null;
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        interactor.deleteIngredient(ingredient);
    }

    @Override
    public void deleteIngredients(List<Ingredient> ingredients) {
        try {
            interactor.deleteIngredients(ingredients);
        } finally {
            createIngredientList();
        }
    }

    @Override
    public void searchIngredientList(String ingredientName) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            interactor.searchIngredients(ingredientName, ingredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
        }
    }
}
