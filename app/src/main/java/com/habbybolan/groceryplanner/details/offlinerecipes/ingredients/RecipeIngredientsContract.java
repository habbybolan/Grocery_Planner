package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsContract {

    interface Presenter<T extends IngredientsView> {
        void setView(T view);
        void destroy();

        /**
         * Get all Ingredient objects associated with the recipe from the offline database.
         * @param recipe   The recipe associated with the Ingredients to display
         */
        void createIngredientList(OfflineRecipe recipe);

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param recipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         */
        void searchIngredients(OfflineRecipe recipe, String ingredientSearch);
    }

    interface PresenterEdit extends Presenter<IngredientsView> {
        /**
         * Delete an ingredient from the recipe
         * @param recipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(OfflineRecipe recipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param recipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(OfflineRecipe recipe, List<Ingredient> ingredients);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly extends Presenter<IngredientsView> {}

    interface Interactor {
        /**
         * Get all Ingredient objects associated with Recipe from the database.
         * @param recipe     The ingredient to display
         * @param callback   callback that updates the ingredients feteched
         */
        void fetchIngredients(OfflineRecipe recipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param recipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         * @param callback           callback to update the list of ingredients showing
         */
        void searchIngredients(OfflineRecipe recipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
    }

    interface InteractorEdit extends Interactor {
        /**
         * Delete an ingredient from the recipe
         * @param recipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(OfflineRecipe recipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param recipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(OfflineRecipe recipe, List<Ingredient> ingredients);

        /**
         * Delete the Recipe.
         * @param recipe Recipe to delete
         */
        void deleteRecipe(OfflineRecipe recipe);
    }

    interface IngredientsView  {
        void loadingStarted();
        void loadingFailed(String message);

        SortType getSortType();

        void showList(List<Ingredient> ingredients);
    }

    interface IngredientsEditView extends ListViewInterface<Ingredient>, IngredientsView {
    }

    interface IngredientsReadOnlyView extends IngredientsView {

        void showList(List<Ingredient> ingredients);
    }

    interface RecipeIngredientsListener {
        OfflineRecipe getRecipe();
    }

    interface RecipeIngredientsMyRecipeListener extends RecipeIngredientsListener{
        void onSwapViewIngredients();
        void onSync();
    }

    interface RecipeIngredientsLikedRecipeListener extends RecipeIngredientsListener{
    }
}
