package com.habbybolan.groceryplanner.details.ingredientedit;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.IngredientHolder;

public class IngredientEditInteractorImpl implements IngredientEditInteractor {

    private DatabaseAccess databaseAccess;
    public IngredientEditInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        databaseAccess.addIngredient(ingredientHolder, ingredient);
    }

    @Override
    public void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        databaseAccess.deleteIngredient(ingredientHolder, ingredient);
    }

    @Override
    public boolean isNewIngredient(Ingredient ingredient) {
        return ingredient.getName().equals("");
    }
}
