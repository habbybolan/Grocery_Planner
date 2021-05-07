package com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation;

import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

public interface IngredientLocationPresenter {

    /**
     * Delete the direct relationship of the ingredient with the grocery.
     * @param grocery           Grocery holding the direct relationship with the ingredient
     * @param ingredient        Ingredient with a direct relationship with the grocery.
     */
    void deleteDirectRelationship(Grocery grocery, GroceryIngredient ingredient);

    /**
     * Deletes the relationship of the ingredient in the grocery through a recipe.
     * @param grocery               grocery holding the recipe ingredient
     * @param ingredient            recipe ingredient in the grocery
     * @param position              position inside ingredient's list of recipe creating the ingredient grocery relationship
     */
    void deleteRecipeRelationship(Grocery grocery, GroceryIngredient ingredient, int position);
}
