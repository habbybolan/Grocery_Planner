package com.habbybolan.groceryplanner.details.myrecipe.ingredients;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsContract {

    interface Presenter<T extends IngredientsView> {
        void setView(T view);
        void destroy();

        /**
         * Get all Ingredient objects associated with recipe from the database.
         * @param myRecipe   The recipe associated with the Ingredients to display
         */
        void createIngredientList(MyRecipe myRecipe);

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param myRecipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         */
        void searchIngredients(MyRecipe myRecipe, String ingredientSearch);
    }

    interface PresenterEdit extends Presenter<IngredientsView> {
        /**
         * Delete an ingredient from the recipe
         * @param myRecipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(MyRecipe myRecipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param myRecipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(MyRecipe myRecipe, List<Ingredient> ingredients);
    }

    interface PresenterReadOnly extends Presenter<IngredientsView> {

    }

    interface Interactor {
        /**
         * Get all Ingredient objects associated with Recipe from the database.
         * @param myRecipe     The ingredient to display
         * @param callback   callback that updates the ingredients feteched
         */
        void fetchIngredients(MyRecipe myRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param myRecipe             recipe to search in for the ingredient
         * @param ingredientSearch   ingredient to search for
         * @param callback           callback to update the list of ingredients showing
         */
        void searchIngredients(MyRecipe myRecipe, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException;
    }

    interface InteractorEdit extends Interactor {
        /**
         * Delete an ingredient from the recipe
         * @param myRecipe        The recipe to delete the ingredient from
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(MyRecipe myRecipe, Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param myRecipe        The recipe to delete the ingredients from
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(MyRecipe myRecipe, List<Ingredient> ingredients);

        /**
         * Delete the Recipe.
         * @param myRecipe Recipe to delete
         */
        void deleteRecipe(MyRecipe myRecipe);
    }

    interface IngredientsView  {
        void loadingStarted();
        void loadingFailed(String message);

        SortType getSortType();

        void showList(List<Ingredient> ingredients);
    }

    interface IngredientsEditView extends ListViewInterface<Ingredient>, IngredientsView {

        /**
         * Updates the shared OfflineRecipe and updates the displayed list with showList() super method.
         * @param ingredients   Ingredients to display and update OfflineRecipe with
         */
        void onListFetched(List<Ingredient> ingredients);
    }

    interface IngredientsReadOnlyView extends IngredientsView {

        void showList(List<Ingredient> ingredients);
    }

    interface RecipeIngredientsListener {
        MyRecipe getMyRecipe();
        void onSwapViewIngredients();
    }
}
