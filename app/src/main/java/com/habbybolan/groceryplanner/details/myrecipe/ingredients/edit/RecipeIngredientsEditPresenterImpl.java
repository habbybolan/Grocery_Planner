package com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit;

import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.List;

public class RecipeIngredientsEditPresenterImpl extends RecipeIngredientsPresenterImpl<RecipeIngredientsContract.InteractorEdit, RecipeIngredientsContract.IngredientsView> implements RecipeIngredientsContract.PresenterEdit{

    public RecipeIngredientsEditPresenterImpl(RecipeIngredientsContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void deleteIngredient(MyRecipe myRecipe, Ingredient ingredient) {
        interactor.deleteIngredient(myRecipe, ingredient);
        createIngredientList(myRecipe);
    }

    @Override
    public void deleteIngredients(MyRecipe myRecipe, List<Ingredient> ingredients) {
        interactor.deleteIngredients(myRecipe, ingredients);
        createIngredientList(myRecipe);
    }
}
