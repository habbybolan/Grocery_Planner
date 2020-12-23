package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListInteractorImpl implements RecipeListInteractor{

    private DatabaseAccess databaseAccess;

    public RecipeListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public List<Recipe> fetchRecipes() throws ExecutionException, InterruptedException {
        return databaseAccess.fetchRecipes();
    }

    @Override
    public List<Recipe> searchRecipes(String search) {
        // todo:
        return null;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }
}
