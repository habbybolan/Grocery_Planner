package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;

public interface IngredientListPresenter {

    /**
     * Creates view inside presenter to allow methods to be called from Presenter implemented inside IngredientListFragment.
     * Calls method to set up the beginning of the ingredient list.
     * @param view  Methods implemented inside IngredientListFragment
     */
    void setView(ListViewInterface view);

    /**
     * Create the ingredient list by accessing local/online database.
     * Call for the list to be displayed if the view is connected.
     */
    void createIngredientList();

    /**
     * Destroy the Presenter.
     */
    void destroy();

    /**
     * Delete an ingredient from database
     * @param ingredient   ingredient to delete
     */
    void deleteIngredient(Ingredient ingredient);

    /**
     * Delete a list of ingredients
     * @param ingredients ingredients to delete
     */
    void deleteIngredients(List<Ingredient> ingredients);

    /**
     * Find Ingredient objects based on the ingredientName typed by user.
     * @param ingredientName   The name of the ingredient object to try to retrieve.
     */
    void searchIngredientList(String ingredientName);
}
