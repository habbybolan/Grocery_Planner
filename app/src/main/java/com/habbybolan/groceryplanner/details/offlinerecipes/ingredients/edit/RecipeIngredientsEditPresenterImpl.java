package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit;

import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;

public class RecipeIngredientsEditPresenterImpl extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.InteractorEdit, RecipeIngredientsContract.IngredientsView> implements RecipeIngredientsContract.PresenterEdit{

    public RecipeIngredientsEditPresenterImpl(RecipeIngredientsContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void deleteIngredient(OfflineRecipe recipe, Ingredient ingredient) {
        interactor.deleteIngredient(recipe, ingredient);
        createIngredientList(recipe);
    }

    @Override
    public void deleteIngredients(OfflineRecipe recipe, List<Ingredient> ingredients) {
        interactor.deleteIngredients(recipe, ingredients);
        createIngredientList(recipe);
    }
}
