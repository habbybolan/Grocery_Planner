package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryIngredientsPresenterImpl implements GroceryIngredientsPresenter {

    private GroceryIngredientsInteractor groceryIngredientsInteractor;
    private ListViewInterface view;

    // true if the ingredients are being loaded
    private boolean loadingIngredients = false;

    private List<GroceryIngredient> loadedIngredients = new ArrayList<>();

    private DbCallback<GroceryIngredient> groceryIngredientDbCallback = new DbCallback<GroceryIngredient>() {
        @Override
        public void onResponse(List<GroceryIngredient> response) {
            // set the loaded ingredients as loaded in
            loadedIngredients.clear();
            loadedIngredients.addAll(response);
            loadingIngredients = false;
            displayIngredients();
        }
    };

    @Inject
    public GroceryIngredientsPresenterImpl(GroceryIngredientsInteractor groceryIngredientsInteractor) {
        this.groceryIngredientsInteractor = groceryIngredientsInteractor;
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
        groceryIngredientsInteractor.deleteGrocery(grocery);
    }

    @Override
    public void deleteIngredient(Grocery grocery, Ingredient ingredient) {
        groceryIngredientsInteractor.deleteIngredient(grocery, ingredient);
        createIngredientList(grocery);
    }

    @Override
    public void deleteIngredients(Grocery grocery, List<GroceryIngredient> ingredients) {
        try {
            groceryIngredientsInteractor.deleteIngredients(grocery, ingredients);
        } finally {
            createIngredientList(grocery);
        }
    }

    /**
     * Check if the ingredients are being loaded.
     * @return  true if the ingredients have already loaded
     */
    private boolean isIngredientsReady() {
        return !loadingIngredients;
    }

    /**
     * Display the grocery ingredients.
     */
    private void displayIngredients() {
        if (isViewAttached() && isIngredientsReady())
            view.showList(loadedIngredients);
    }

    @Override
    public void createIngredientList(Grocery grocery) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            groceryIngredientsInteractor.fetchIngredients(grocery, groceryIngredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
        }
    }

    @Override
    public List<GroceryIngredient> getIngredients() {
        return loadedIngredients;
    }

    @Override
    public void updateGroceryIngredientSelected(Grocery grocery, GroceryIngredient groceryIngredient) {
        groceryIngredientsInteractor.updateGroceryIngredientSelected(grocery, groceryIngredient);
    }

    @Override
    public void searchIngredients(Grocery grocery, String ingredientSearch) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            groceryIngredientsInteractor.searchIngredients(grocery, ingredientSearch, groceryIngredientDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve data");
            loadingIngredients = false;
        }
    }
}
