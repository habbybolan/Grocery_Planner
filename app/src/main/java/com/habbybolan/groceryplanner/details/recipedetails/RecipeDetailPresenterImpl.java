package com.habbybolan.groceryplanner.details.recipedetails;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private RecipeDetailInteractor recipeDetailInteractor;
    private ListViewInterface view;
    private List<Ingredient> loadedIngredients = new ArrayList<>();

    public RecipeDetailPresenterImpl(RecipeDetailInteractor recipeDetailInteractor) {
        this.recipeDetailInteractor =recipeDetailInteractor;
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
        try {
            loadedIngredients = recipeDetailInteractor.fetchIngredients(recipe);
            if (isViewAttached()) view.showList(loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredient");
        }
    }

    @Override
    public void deleteIngredients(Recipe recipe, List<Ingredient> ingredients) {
        recipeDetailInteractor.deleteIngredients(recipe, ingredients);
        try {
            loadedIngredients = recipeDetailInteractor.fetchIngredients(recipe);
            if (isViewAttached()) view.showList(loadedIngredients);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to delete Ingredients");
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void createIngredientList(Recipe recipe) {
        try {
            if (isViewAttached()) {
                view.showList(recipeDetailInteractor.fetchIngredients(recipe));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
