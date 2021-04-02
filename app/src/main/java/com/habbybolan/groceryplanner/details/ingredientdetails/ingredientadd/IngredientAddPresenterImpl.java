package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddPresenterImpl implements IngredientAddPresenter {

    private ObservableArrayList<Ingredient> loadedIngredients =  new ObservableArrayList<>();
    private IngredientAddView view;
    private IngredientAddInteractor interactor;
    private boolean loadingIngredients = false;
    private HashSet<Ingredient> selectedIngredients = new HashSet<>();

    @Inject
    public IngredientAddPresenterImpl(IngredientAddInteractor interactor) {
        this.interactor = interactor;
        setIngredientCallback();
    }

    private void setIngredientCallback() {
        loadedIngredients.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Ingredient>>() {
            @Override
            public void onChanged(ObservableList<Ingredient> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}

            @Override
            public void onItemRangeInserted(ObservableList<Ingredient> sender, int positionStart, int itemCount) {
                loadingIngredients = false;
                displayIngredientsToAdd();
            }

            @Override
            public void onItemRangeMoved(ObservableList<Ingredient> sender, int fromPosition, int toPosition, int itemCount) {}

            @Override
            public void onItemRangeRemoved(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}
        });
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
            interactor.fetchIngredientsNotInIngredientHolder(loadedIngredients, ingredientHolder);
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
