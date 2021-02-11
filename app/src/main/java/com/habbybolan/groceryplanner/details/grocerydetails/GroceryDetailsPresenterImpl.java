package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryDetailsPresenterImpl implements GroceryDetailsPresenter {

    private GroceryDetailsInteractor groceryDetailsInteractor;
    private ListViewInterface view;
    private List<Ingredient> loadedIngredients = new ArrayList<>();

    @Inject
    public GroceryDetailsPresenterImpl(GroceryDetailsInteractor groceryDetailsInteractor) {
        this.groceryDetailsInteractor = groceryDetailsInteractor;
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    /**
     * Check if view is attached to communicate with fragment.
     * @return  True if the GroceryListFragment view interface is added to presenter
     */
    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void editGroceryName(Grocery grocery, String name) {
        // todo:
    }

    @Override
    public void deleteGrocery(Grocery grocery) {
        groceryDetailsInteractor.deleteGrocery(grocery);
    }

    @Override
    public void deleteIngredient(Grocery grocery, Ingredient ingredient) {
        groceryDetailsInteractor.deleteIngredient(grocery, ingredient);
        try {
            loadedIngredients = groceryDetailsInteractor.fetchIngredients(grocery);
            if (isViewAttached()) view.showList(loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredient");
        }
    }

    @Override
    public void deleteIngredients(Grocery grocery, List<Ingredient> ingredients) {
        groceryDetailsInteractor.deleteIngredients(grocery, ingredients);
        try {
            loadedIngredients = groceryDetailsInteractor.fetchIngredients(grocery);
            if (isViewAttached()) view.showList(loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredients");
        }
    }

    @Override
    public void createIngredientList(Grocery grocery) {
        try {
            loadedIngredients = groceryDetailsInteractor.fetchIngredients(grocery);
            if (isViewAttached()) {
                view.showList(loadedIngredients);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
