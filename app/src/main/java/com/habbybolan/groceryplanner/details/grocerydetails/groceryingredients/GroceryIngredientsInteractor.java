package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroceryIngredientsInteractor {

    void editGroceryName(Grocery grocery, String name);
    void deleteGrocery(Grocery grocery);

    /**
     * Get all Grocery Ingredient objects associated with grocery from the database.
     * @param grocery               The grocery associated with the Ingredients to return
     * @param ingredientsObserver   Grocery Ingredient list to observe
     */
    void fetchIngredients(Grocery grocery, ObservableArrayList<GroceryIngredient> ingredientsObserver) throws ExecutionException, InterruptedException;

    /**
     * Delete an ingredient from the grocery
     * @param grocery       The grocery to delete the ingredient from
     * @param ingredient    The ingredient to delete
     */
    void deleteIngredient(Grocery grocery, Ingredient ingredient);

    /**
     * Delete ingredients from the grocery
     * @param grocery       The grocery to delete the ingredients from
     * @param ingredients   The ingredients to delete
     */
    void deleteIngredients(Grocery grocery, List<GroceryIngredient> ingredients);

    /**
     * Update the ingredients bridges with new isChecked
     * @param grocery               Grocery holding the ingredient to update
     * @param groceryIngredient     holds the check value to change it to
     */
    void updateGroceryIngredientSelected(Grocery grocery, GroceryIngredient groceryIngredient);
}
