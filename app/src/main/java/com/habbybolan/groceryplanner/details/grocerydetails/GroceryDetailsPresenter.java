package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public interface GroceryDetailsPresenter {

    void setView(ListViewInterface view);
    void destroy();

    /**
     * Edits the grocery name.
     * @param grocery   Grocery to edit
     * @param name      New name to set
     */
    void editGroceryName(Grocery grocery, String name);

    /**
     * Delete this grocery.
     * @param grocery   The grocery to delete.
     */
    void deleteGrocery(Grocery grocery);

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

    /**
     * Get all Ingredient objects associated with grocery from the database.
     * @param grocery   The grocery associated with the Ingredients to display
     */
    void createIngredientList(Grocery grocery);
}
