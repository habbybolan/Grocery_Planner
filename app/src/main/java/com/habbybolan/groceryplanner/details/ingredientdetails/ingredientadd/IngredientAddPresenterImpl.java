package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddPresenterImpl implements IngredientAddPresenter {

    private List<Ingredient> loadedIngredients =  new ArrayList<>();
    private IngredientAddView view;
    private IngredientAddInteractor interactor;

    private DbCallback<Ingredient> ingredientDbCallback = new DbCallback<Ingredient>() {
        @Override
        public void onResponse(List<Ingredient> response) {
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            displayExistingIngredients();
        }
    };

    @Inject
    public IngredientAddPresenterImpl(IngredientAddInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(IngredientAddView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void fetchIngredientsNotInIngredientHolder(OfflineIngredientHolder ingredientHolder, String search) {
        try {
            interactor.fetchIngredientsNotInIngredientHolder(ingredientDbCallback, ingredientHolder, search);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayExistingIngredients() {
        view.showListOfIngredients(loadedIngredients);
    }

    @Override
    public void addIngredientToIngredientHolder(Ingredient ingredient, OfflineIngredientHolder ingredientHolder) {
        interactor.addIngredientToIngredientHolder(ingredient, ingredientHolder, new DbSingleCallbackWithFail<Ingredient>() {
            @Override
            public void onFail(String message) {
               // TODO:
            }

            @Override
            public void onResponse(Ingredient response) {
                view.leaveFragment(response);
            }
        });
    }

    @Override
    public void addIngredientToIngredientHolder(String ingredientName, OfflineIngredientHolder ingredientHolder) {
        interactor.addIngredientToIngredientHolder(ingredientName, ingredientHolder, new DbSingleCallbackWithFail<Ingredient>() {
            @Override
            public void onFail(String message) {
                // TODO:
            }

            @Override
            public void onResponse(Ingredient response) {
                view.leaveFragment(response);
            }
        });
    }
}
