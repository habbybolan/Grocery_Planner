package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeIngredientsInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeIngredientsInteractor {

    public RecipeIngredientsInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe.getId());
    }

    @Override
    public void fetchIngredients(Recipe recipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredientsFromRecipe(recipe, sortType, callback);
    }

    @Override
    public void deleteIngredient(Recipe recipe, Ingredient ingredient) {
        databaseAccess.deleteIngredientFromHolder(recipe, ingredient.getId());
    }

    @Override
    public void deleteIngredients(Recipe recipe, List<Ingredient> ingredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredientsFromHolder(recipe, ingredientIds);
    }

    @Override
    public void searchIngredients(Recipe recipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchRecipeIngredients(recipe.getId(), ingredientSearch, callback);
    }
}
