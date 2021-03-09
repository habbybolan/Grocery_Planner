package com.habbybolan.groceryplanner.details.recipe.recipedetails;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private RecipeDetailInteractor recipeDetailInteractor;
    private ListViewInterface view;

    // true if the ingredients are being loaded
    private boolean loadingIngredients = false;
    private ObservableArrayList<Ingredient> loadedIngredients = new ObservableArrayList<>();

    public RecipeDetailPresenterImpl(RecipeDetailInteractor recipeDetailInteractor) {
        this.recipeDetailInteractor = recipeDetailInteractor;
        setIngredientsCallback();
    }

    // set up callback for loading ingredients
    private void setIngredientsCallback() {
        loadedIngredients.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Ingredient>>() {
            @Override
            public void onChanged(ObservableList<Ingredient> sender) {}
            @Override
            public void onItemRangeChanged(ObservableList<Ingredient> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<Ingredient> sender, int positionStart, int itemCount) {
                // set the loaded ingredients as loaded in
                loadingIngredients = false;
                displayIngredientList();
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
    public void destroy() {
        view = null;
    }

    @Override
    public void editRecipeName(Recipe recipe, String name) {
        // todo:
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeDetailInteractor.deleteRecipe(recipe);
    }

    @Override
    public void deleteIngredient(Recipe recipe, Ingredient ingredient) {
        recipeDetailInteractor.deleteIngredient(recipe, ingredient);
        createIngredientList(recipe);
    }

    @Override
    public void deleteIngredients(Recipe recipe, List<Ingredient> ingredients) {
        recipeDetailInteractor.deleteIngredients(recipe, ingredients);
        createIngredientList(recipe);
    }

    /**
     * Signals if the ingredients are being loaded in.
     * @return  True if the ingredients are loaded in
     */
    private boolean isIngredientsReady() {
        return !loadingIngredients;
    }

    private void displayIngredientList() {
        if (isViewAttached() && isIngredientsReady())
            view.showList(loadedIngredients);
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void createIngredientList(Recipe recipe) {
        try {
            loadingIngredients = true;
            view.loadingStarted();
            recipeDetailInteractor.fetchIngredients(recipe, loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredients");
            loadingIngredients = false;
        }
    }
}
