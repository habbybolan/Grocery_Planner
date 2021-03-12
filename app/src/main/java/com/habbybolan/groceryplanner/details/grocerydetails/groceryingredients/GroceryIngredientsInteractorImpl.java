package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.database.DatabaseAccess;

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
    public void fetchIngredients(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        databaseAccess.fetchIngredientsFromGrocery(grocery, ingredientsObserver);
    }

    @Override
    public void deleteIngredient(Grocery grocery, Ingredient ingredient) {
        databaseAccess.deleteIngredient(grocery, ingredient);
    }

    @Override
    public void deleteIngredients(Grocery grocery, List<Ingredient> ingredients) {
        databaseAccess.deleteIngredients(grocery, ingredients);
    }
}
