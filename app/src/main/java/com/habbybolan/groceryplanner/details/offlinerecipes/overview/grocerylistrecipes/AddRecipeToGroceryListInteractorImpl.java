package com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class AddRecipeToGroceryListInteractorImpl implements AddRecipeToGroceryListContract.Interactor {

    private DatabaseAccess databaseAccess;

    @Inject
    public AddRecipeToGroceryListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchGroceriesHoldingRecipe(OfflineRecipe recipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesHoldingRecipe(recipe, callback);
    }

    @Override
    public List<IngredientWithGroceryCheck> checkIfAllUnselected(List<IngredientWithGroceryCheck> ingredients) {
        boolean allUnselected = true;
        // find if all the ingredients are in the Grocery or not
        for (IngredientWithGroceryCheck ingredient : ingredients) {
            if (ingredient.getIsInGrocery()) {
                allUnselected = false;
                break;
            }
        }
        // if all ingredients not in grocery, then its recipe is not yet added to the grocery list
        // start the list with all ingredients selected
        if (allUnselected) {
            for (IngredientWithGroceryCheck ingredient : ingredients) {
                ingredient.setIsInGrocery(true);
            }
        }
        return ingredients;
    }
    @Override
    public void fetchGroceriesNotHoldingRecipe(OfflineRecipe myRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesNotHoldingRecipe(myRecipe, callback);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(OfflineRecipe myRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        // loop through the ingredients and find if all of them are not added to Grocery list
        // if none added, then delete the recipe from the GroceryRecipeBridge
        boolean hasIngredientSelected = false;
        for (IngredientWithGroceryCheck ingredient : ingredients) {
            if (ingredient.getIsInGrocery()) {
                hasIngredientSelected = true;
                break;
            }
        }
        // if no ingredient inside the grocery list, then remove the recipe grocery relationship
        if (!hasIngredientSelected)
            databaseAccess.deleteGroceryRecipeBridge(myRecipe, grocery);
        else
            // otherwise, update update or add the recipe and its selected/unselected ingredients to the grocery list
            databaseAccess.updateRecipeIngredientsInGrocery(myRecipe, grocery, amount, ingredients);
    }

    @Override
    public void deleteRecipeFromGrocery(OfflineRecipe myRecipe, Grocery grocery) {
        databaseAccess.deleteRecipeFromGrocery(grocery, myRecipe);
    }

    @Override
    public void fetchRecipeIngredients(OfflineRecipe myRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        if (isNotInGrocery) {
            databaseAccess.fetchRecipeIngredientsNotInGrocery(myRecipe, callback);
        } else {
            databaseAccess.fetchRecipeIngredientsInGrocery(grocery, myRecipe, callback);
        }
    }

    @Override
    public String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients) {
        String[] ingredientNames = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientNames[i] = ingredients.get(i).getName();
        }
        return ingredientNames;
    }

    @Override
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        String[] groceryNames = new String[groceries.size()];
        for (int i = 0; i < groceries.size(); i++) {
            groceryNames[i] = groceries.get(i).getName();
        }
        return groceryNames;
    }
}
