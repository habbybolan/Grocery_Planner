package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsContract {

    interface Presenter {
        void setView(RecipeIngredientsContract.IngredientsView view);
        void destroy();

        /**
         * Edits the Recipe name.
         * @param offlineRecipe    Recipe to edit
         * @param name      New name to set
         */
        void editRecipeName(OfflineRecipe offlineRecipe, String name);

        /**
         * Delete an ingredient from the recipe
         * @param offlineRecipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param offlineRecipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients);

        /**
         * Get all Ingredient objects associated with recipe from the database.
         * @param offlineRecipe   The recipe associated with the Ingredients to display
         */
        void createIngredientList(OfflineRecipe offlineRecipe);

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param offlineRecipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         */
        void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch);
    }

    interface Interactor {
        /**
         * Get all Ingredient objects associated with Recipe from the database.
         * @param offlineRecipe     The ingredient to display
         * @param callback   callback that updates the ingredients feteched
         */
        void fetchIngredients(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

        /**
         * Delete an ingredient from the recipe
         * @param offlineRecipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(OfflineRecipe offlineRecipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param offlineRecipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(OfflineRecipe offlineRecipe, List<Ingredient> ingredients);

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param offlineRecipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         * @param callback           callback to update the list of ingredients showing
         */
        void searchIngredients(OfflineRecipe offlineRecipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

        /**
         * Delete the Recipe.
         * @param offlineRecipe Recipe to delete
         */
        void deleteRecipe(OfflineRecipe offlineRecipe);
    }

    interface IngredientsView extends ListViewInterface<Ingredient> {

        /**
         * Updates the shared OfflineRecipe and updates the displayed list with showList() super method.
         * @param ingredients   Ingredients to display and update OfflineRecipe with
         */
        void onListFetched(List<Ingredient> ingredients);
    }
}
