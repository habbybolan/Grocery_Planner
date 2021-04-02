package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.database.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GroceryIngredientsInteractorImpl implements GroceryIngredientsInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public GroceryIngredientsInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void editGroceryName(Grocery grocery, String name) {
        // todo:
    }

    @Override
    public void deleteGrocery(Grocery grocery) {
        databaseAccess.deleteGrocery(grocery);
    }

    @Override
    public void fetchIngredients(Grocery grocery, ObservableArrayList<GroceryIngredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceryIngredients(grocery, ingredientsObserver);
    }

    @Override
    public void deleteIngredient(Grocery grocery, Ingredient ingredient) {
        databaseAccess.deleteIngredient(grocery, ingredient);
    }

    @Override
    public void deleteIngredients(Grocery grocery, List<GroceryIngredient> groceryIngredients) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (GroceryIngredient groceryIngredient : groceryIngredients) {
            ingredients.add(groceryIngredient.getIngredient());
        }
        databaseAccess.deleteIngredients(grocery, ingredients);
    }

    @Override
    public void updateGroceryIngredient(Grocery grocery, GroceryIngredient groceryIngredient) {
        databaseAccess.updateGroceryIngredientChecked(grocery, groceryIngredient);
    }
}
