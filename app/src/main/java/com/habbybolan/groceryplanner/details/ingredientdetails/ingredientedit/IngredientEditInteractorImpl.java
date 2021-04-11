package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

public class IngredientEditInteractorImpl implements IngredientEditInteractor {

    private DatabaseAccess databaseAccess;
    public IngredientEditInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder != null)
            databaseAccess.addIngredient(ingredientHolder, ingredient);
        else
            databaseAccess.addIngredient(ingredient);
    }

    @Override
    public void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder != null)
            databaseAccess.deleteIngredientFromHolder(ingredientHolder, ingredient);
        else
            databaseAccess.deleteIngredient(ingredient.getId());
    }

    @Override
    public boolean isNewIngredient(Ingredient ingredient) {
        return ingredient.getName().equals("");
    }

    @Override
    public void deleteRelationship(IngredientHolder ingredientHolder, Ingredient ingredient) {
        databaseAccess.deleteIngredientHolderRelationship(ingredientHolder, ingredient);
    }
}
