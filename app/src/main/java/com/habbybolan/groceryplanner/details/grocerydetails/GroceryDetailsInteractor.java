package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroceryDetailsInteractor {

    void editGroceryName(Grocery grocery, String name);
    void deleteGrocery(Grocery grocery);
    /**
     * Get all Ingredient objects associated with grocery from the database.
     * @param grocery   The grocery associated with the Ingredients to return
     * @return          All Ingredients associated with grocery
     */
    List<Ingredient> fetchIngredients(Grocery grocery) throws ExecutionException, InterruptedException;

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
    void deleteIngredients(Grocery grocery, List<Ingredient> ingredients);
}
