package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class IngredientAddInteractorImpl implements IngredientAddInteractor {

    private DatabaseAccess databaseAccess;

    @Inject
    public IngredientAddInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchIngredientsNotInIngredientHolder(ObservableArrayList<Ingredient> ingredientsObserver, IngredientHolder ingredientHolder) throws ExecutionException, InterruptedException {
        if (ingredientHolder.isGrocery())
            databaseAccess.fetchIngredientsNotInGrocery((Grocery) ingredientHolder, ingredientsObserver);
        else
            databaseAccess.fetchIngredientsNotInRecipe((Recipe) ingredientHolder, ingredientsObserver);
    }

    @Override
    public void addCheckedToIngredientHolder(HashSet<Ingredient> checkedIngredients, IngredientHolder ingredientHolder) {
        List<Ingredient> checkIngredientsArray = new ArrayList<>(checkedIngredients);
        databaseAccess.addIngredients(ingredientHolder, checkIngredientsArray);
    }
}
