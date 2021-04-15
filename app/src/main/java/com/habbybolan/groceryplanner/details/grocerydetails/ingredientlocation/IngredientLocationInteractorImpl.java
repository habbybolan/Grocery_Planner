package com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

public class IngredientLocationInteractorImpl implements IngredientLocationInteractor {

    private DatabaseAccess databaseAccess;

    public IngredientLocationInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void deleteDirectRelationship(Grocery grocery, GroceryIngredient ingredient) {
        // delete the direct ingredient relationship from the grocery
        databaseAccess.deleteDirectIngredientFromGrocery(grocery.getId(), ingredient.getId());
        // delete the relationship from the ingredient field
        ingredient.setIsDirectRelationship(false);
    }

    @Override
    public void deleteRecipeRelationship(Grocery grocery, GroceryIngredient ingredient, int position) {
        // delete recipe ingredient relationship with grocery in database
        databaseAccess.deleteRecipeIngredientFromGrocery(grocery.getId(), ingredient.getRecipeWithIngredients().get(position).getRecipeId(), ingredient.getId());
        // delete the relationship inside the list
        ingredient.getRecipeWithIngredients().remove(position);
    }
}
