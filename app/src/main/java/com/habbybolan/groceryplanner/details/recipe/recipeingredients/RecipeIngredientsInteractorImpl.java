package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeIngredientsInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeIngredientsInteractor {

    public RecipeIngredientsInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        databaseAccess.deleteRecipe(offlineRecipe.getId());
    }

    @Override
    public void fetchIngredients(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredientsFromRecipe(offlineRecipe, sortType, callback);
    }

    @Override
    public void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient) {
        databaseAccess.deleteIngredientsFromRecipe(offlineRecipe.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
    }

    @Override
    public void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredientsFromRecipe(offlineRecipe.getId(), ingredientIds);
    }

    @Override
    public void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchRecipeIngredients(offlineRecipe.getId(), ingredientSearch, callback);
    }
}
