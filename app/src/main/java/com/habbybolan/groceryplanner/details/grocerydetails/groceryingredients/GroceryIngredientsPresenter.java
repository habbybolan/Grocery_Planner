package com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;

public interface GroceryIngredientsPresenter {

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
    void deleteIngredients(Grocery grocery, List<GroceryIngredient> ingredients);

    /**
     * Get all Ingredient objects associated with grocery from the database.
     * @param grocery   The grocery associated with the Ingredients to display
     */
    void createIngredientList(Grocery grocery);

    /**
     * Get all loaded ingredients
     * @return  Loaded ingredients
     */
    List<GroceryIngredient> getIngredients();

    /**
     * Update the ingredients bridges with new isChecked
     * @param grocery               Grocery holding the ingredient to update
     * @param groceryIngredient     holds the check value to change it to
     */
    void updateGroceryIngredientSelected(Grocery grocery, GroceryIngredient groceryIngredient);

    /**
     * Search for the grocery ingredients with name ingredientSearch.
     * @param grocery             grocery to search in for the ingredient
     * @param ingredientSearch   ingredient to search for
     */
    void searchIngredients(Grocery grocery, String ingredientSearch);

    void setSortType(int sortTypeId);
}
