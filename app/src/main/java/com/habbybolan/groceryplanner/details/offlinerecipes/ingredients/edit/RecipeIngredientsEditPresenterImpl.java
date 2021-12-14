package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit;

import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.List;

public class RecipeIngredientsEditPresenterImpl
        extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.InteractorEdit, RecipeIngredientsContract.IngredientsView,
            MyRecipe>
        implements RecipeIngredientsContract.PresenterEdit{

    public RecipeIngredientsEditPresenterImpl(RecipeIngredientsContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        interactor.deleteIngredient(recipe, ingredient);
        createIngredientList();
    }

    @Override
    public void deleteIngredients(List<Ingredient> ingredients) {
        interactor.deleteIngredients(recipe, ingredients);
        createIngredientList();
    }
}
