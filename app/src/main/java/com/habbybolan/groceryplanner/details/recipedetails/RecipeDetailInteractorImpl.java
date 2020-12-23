package com.habbybolan.groceryplanner.details.recipedetails;

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
    public List<Ingredient> fetchIngredients(Recipe recipe) throws ExecutionException, InterruptedException {
        return databaseAccess.fetchIngredientsFromRecipe(recipe);
    }
}
