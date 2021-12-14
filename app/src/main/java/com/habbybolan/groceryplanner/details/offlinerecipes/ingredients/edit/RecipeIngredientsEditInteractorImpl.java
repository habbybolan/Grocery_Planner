package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeIngredientsEditInteractorImpl
        extends RecipeIngredientsInteractorImpl<MyRecipe> implements RecipeIngredientsContract.InteractorEdit {

    public RecipeIngredientsEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void deleteRecipe(OfflineRecipe recipe) {
        databaseAccess.deleteRecipe(recipe.getId());
    }

    @Override
    public void deleteIngredient(OfflineRecipe recipe, Ingredient ingredient) {
        databaseAccess.deleteIngredientsFromRecipe(recipe.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
    }

    @Override
    public void deleteIngredients(OfflineRecipe recipe, List<Ingredient> ingredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredientsFromRecipe(recipe.getId(), ingredientIds);
    }

    @Override
    public void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<MyRecipe> callback) {
        try {
            databaseAccess.fetchFullMyRecipe(recipeId, callback);
        } catch (InterruptedException | ExecutionException e) {
            callback.onFail("");
        }
    }
}
