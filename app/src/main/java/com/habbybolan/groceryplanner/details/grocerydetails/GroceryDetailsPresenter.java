package com.habbybolan.groceryplanner.details.grocerydetails;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Grocery;

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
     * Get all Ingredient objects associated with grocery from the database.
     * @param grocery   The grocery associated with the Ingredients to display
     */
    void createIngredientList(Grocery grocery);
}
