package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryIngredientsPresenterImpl implements GroceryIngredientsPresenter {

    private GroceryIngredientsInteractor groceryIngredientsInteractor;
    private ListViewInterface view;

    // true if the ingredients are being loaded
    private boolean loadingIngredients = false;
    private ObservableArrayList<Ingredient> loadedIngredients = new ObservableArrayList<>();

    @Inject
    public GroceryIngredientsPresenterImpl(GroceryIngredientsInteractor groceryIngredientsInteractor) {
        this.groceryIngredientsInteractor = groceryIngredientsInteractor;
        setGroceryCallback();
    }

    // set up callback for loading ingredients
    private void setGroceryCallback() {
        loadedIngredients.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Ingredient>>() {
            @Override
            public void onChanged(ObservableList<Ingredient> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<Ingredient> sender, int positionStart, int itemCount) {
                // set the loaded ingredients as loaded in
                loadingIngredients = false;
                displayIngredients();
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
    public void deleteIngredients(Grocery grocery, List<Ingredient> ingredients) {
        groceryIngredientsInteractor.deleteIngredients(grocery, ingredients);
        createIngredientList(grocery);
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
            groceryIngredientsInteractor.fetchIngredients(grocery, loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            loadingIngredients = false;
        }
    }

    @Override
    public List<Ingredient> getIngredients() {
        return loadedIngredients;
    }
}
