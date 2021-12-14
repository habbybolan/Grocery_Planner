package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.details.offlinerecipes.DetailFragmentView;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeIngredientsContract {

    interface Presenter<U extends Interactor<T2>, T extends IngredientsView, T2 extends OfflineRecipe> {
        void setupPresenter(T view, long recipeId);
        void destroy();

        /**
         * Get all Ingredient objects associated with the recipe from the offline database.
         */
        void createIngredientList();

        /**
         * Search for the recipe ingredients with name ingredientSearch.
         * @param ingredientSearch   ingredient to search for
         */
        void searchIngredients(String ingredientSearch);

        T2 getRecipe();
        void loadUpdatedRecipe();
    }

    interface PresenterEdit extends Presenter<InteractorEdit, IngredientsView, MyRecipe> {
        /**
         * Delete an ingredient from the recipe
         * @param ingredient    The ingredient to delete
         */
        void deleteIngredient(Ingredient ingredient);

        /**
         * Delete ingredients from the recipe
         * @param ingredients   The ingredients to delete
         */
        void deleteIngredients(List<Ingredient> ingredients);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly<T extends Interactor<U>, U extends OfflineRecipe> extends Presenter<T, IngredientsView, U> {}

    interface PresenterMyRecipe extends PresenterReadOnly<InteractorMyRecipe, MyRecipe> {}
    interface PresenterLikedRecipe extends PresenterReadOnly<InteractorLikedRecipe, LikedRecipe> {}

    interface Interactor<T extends OfflineRecipe> {
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

        void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<T> callback);
    }

    interface InteractorEdit extends Interactor<MyRecipe> {
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

    interface InteractorMyRecipe extends Interactor<MyRecipe> {}

    interface InteractorLikedRecipe extends Interactor<LikedRecipe> {}

    interface IngredientsView extends DetailFragmentView {

        SortType getSortType();

        void showList(List<Ingredient> ingredients);

        /** setup the recipe views */
        void setupRecipeViews();
    }

    interface IngredientsEditView extends ListViewInterface<Ingredient>, IngredientsView {
    }

    interface IngredientsReadOnlyView extends IngredientsView {
    }

    interface RecipeIngredientsListener {
    }

    interface RecipeIngredientsMyRecipeListener extends RecipeIngredientsListener{
        void onSwapViewIngredients();
        void onSync();
    }

    interface RecipeIngredientsLikedRecipeListener extends RecipeIngredientsListener{
    }
}
