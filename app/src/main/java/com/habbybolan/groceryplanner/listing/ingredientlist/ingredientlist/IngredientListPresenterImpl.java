package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientListPresenterImpl implements IngredientListPresenter {

    private IngredientListInteractor interactor;
    private ListViewInterface view;

    // ingredients loaded from the database
    private ObservableArrayList<Ingredient> loadedIngredients = new ObservableArrayList<>();
    // true if the ingredients are being retrieved from the database
    private boolean loadingIngredients = false;

    @Inject
    public IngredientListPresenterImpl(IngredientListInteractor interactor) {
        this.interactor = interactor;
        setIngredientsCallback();
    }

    private void setIngredientsCallback() {
        loadedIngredients.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Ingredient>>() {
            @Override
            public void onChanged(ObservableList<Ingredient> sender) {}
            @Override
            public void onItemRangeChanged(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<Ingredient> sender, int positionStart, int itemCount) {
                loadingIngredients = false;
                view.showList(loadedIngredients);
            }
            @Override
            public void onItemRangeMoved(ObservableList<Ingredient> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}
        });
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
            interactor.fetchIngredients(loadedIngredients);
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
            interactor.searchIngredients(ingredientName, loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
        }
    }
}
