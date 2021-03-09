package com.habbybolan.groceryplanner.details.recipe.recipedetails;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeDetailInteractorImpl implements RecipeDetailInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeDetailInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void editRecipeName(Recipe recipe, String name) {
        // todo:
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe);
    }

    @Override
    public void fetchIngredients(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredientsFromRecipe(recipe, ingredientsObserver);
    }

    @Override
    public void deleteIngredient(Recipe recipe, Ingredient ingredient) {
        databaseAccess.deleteIngredient(recipe, ingredient);
    }

    @Override
    public void deleteIngredients(Recipe recipe, List<Ingredient> ingredients) {
        databaseAccess.deleteIngredients(recipe, ingredients);
    }
}
