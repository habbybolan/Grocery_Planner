package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddPresenterImpl implements IngredientAddPresenter {

    private List<Ingredient> loadedIngredients =  new ArrayList<>();
    private IngredientAddView view;
    private IngredientAddInteractor interactor;
    private boolean loadingIngredients = false;
    private HashSet<Ingredient> selectedIngredients = new HashSet<>();

    private DbCallback<Ingredient> ingredientDbCallback = new DbCallback<Ingredient>() {
        @Override
        public void onResponse(List<Ingredient> response) {
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            loadingIngredients = false;
            displayIngredientsToAdd();
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
    public void fetchIngredientsNotInIngredientHolder(IngredientHolder ingredientHolder) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            interactor.fetchIngredientsNotInIngredientHolder(ingredientDbCallback, ingredientHolder);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
            view.loadingFailed("Failed to retrieve Ingredients");
        }
    }

    @Override
    public void displayIngredientsToAdd() {
        if (isIngredientsReady())
            view.showListOfIngredients(loadedIngredients);
    }

    @Override
    public void addCheckedToIngredientHolder(IngredientHolder ingredientHolder) {
        interactor.addCheckedToIngredientHolder(selectedIngredients, ingredientHolder);
    }

    @Override
    public void selectIngredient(Ingredient ingredient) {
        selectedIngredients.add(ingredient);
    }

    @Override
    public void unSelectIngredient(Ingredient ingredient) {
        selectedIngredients.remove(ingredient);
    }


    private boolean isIngredientsReady() {
        return !loadingIngredients;
    }
}
