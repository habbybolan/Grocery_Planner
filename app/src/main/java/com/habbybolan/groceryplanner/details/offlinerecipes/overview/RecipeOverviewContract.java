package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbCallbackEmptyResponse;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.details.offlinerecipes.DetailFragmentView;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewContract {

    interface Presenter<U extends Interactor<T2>, T extends OverviewView, T2 extends OfflineRecipe> {

        void destroy();

        void setupPresenter(T view, long recipeId);

        /**
         * Get the RecipeCategory at index position of the loaded RecipeCategories.
         * @param position  The position in the array of the RecipeCategory
         * @return          The RecipeCategory at position, null if the last position was selected (No Category)
         */
        RecipeCategory getRecipeCategory(int position);

        /**
         * Fetch the category from the database to display.
         */
        void fetchRecipeCategory();

        T2 getRecipe();

        void loadUpdatedRecipe();
    }

    interface PresenterEdit extends Presenter<InteractorEdit, OverviewEditView, MyRecipe> {

        /**
         * Loads all recipe categories
         */
        void loadAllRecipeCategories();

        /**
         * Returns all the loaded recipe categories
         * @return  loaded recipe categories
         */
        List<RecipeCategory> getAllCategories();

        /**
         * Called display recipe categories if possible
         */
        void displayRecipeCategories();

        /**
         * Delete the tag from the recipe
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(RecipeTag recipeTag);

        /**
         * Checks if the the name for the new RecipeTag is valid.
         * Adds the new tag if valid, otherwise send back error message.
         * @param title      Title of the new RecipeTag being added
         */
        void addRecipeTag(String title);

        /** Calls Interactor to update the recipe values changed. */
        void updateRecipe(String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    /**
     * Wrapper presenters for generic read-only presenters.
     */
    interface PresenterReadOnly<T extends Interactor<U>, U extends OfflineRecipe> extends Presenter<T, OverviewView, U> {}


    interface PresenterMyRecipe extends PresenterReadOnly<InteractorMyRecipeReadOnly, MyRecipe> {}

    interface PresenterLikedRecipe extends PresenterReadOnly<InteractorLikedRecipeReadOnly, LikedRecipe> {}

    interface Interactor<T extends OfflineRecipe> {

        void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;
        void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<T> callback);
    }

    interface InteractorMyRecipeReadOnly extends Interactor<MyRecipe>{}

    interface InteractorLikedRecipeReadOnly extends Interactor<LikedRecipe> {}

    interface InteractorEdit extends Interactor<MyRecipe> {

        void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

        /**
         * Converts the list of RecipeCategory to an array of their names
         * @param recipeCategories  loaded recipe categories
         * @return                  Array of the loaded RecipeCategory names to display
         */
        String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories);


        /**
         * Adds a tag to the recipe.
         * @param callback  callback when the tag is added to the recipe
         * @param title     title of the tag
         * @param myRecipe  recipe to place the tag into
         * @return          True if the recipe tag addition is valid, otherwise false
         */
        void addTag(OfflineRecipe myRecipe, String title, DbSingleCallbackWithFail<RecipeTag> callback);

        /**
         * Delete the tag from the recipe
         * @param myRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         * @param callbackDelete callback for deleting tag
         */
        void deleteRecipeTag(OfflineRecipe myRecipe, RecipeTag recipeTag, DbCallbackEmptyResponse callbackDelete);

        /**
         * Updates the MyRecipe values based on user set values and save the changes to room database.
         * @param myRecipe     MyRecipe to set new values to
         * @param name              Name of recipe to set.
         * @param servingSize       Serving size to set
         * @param cookTime          cook time to set
         * @param prepTime          prep time to set
         * @param description       description to set
         * @param category          category to set
         */
        void updateRecipe(OfflineRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    interface OverviewView extends DetailFragmentView {

        /**
         * Displays the current RecipeCategory in the fragment
         * @param recipeCategory    RecipeCategory's name to display
         */
        void displayRecipeCategory(RecipeCategory recipeCategory);

        /** Display the updated recipe specific values. */
        void displayUpdatedRecipe();

        /** setup the recipe views */
        void setupRecipeViews();
    }

    interface OverviewEditView extends OverviewView, RecipeTagsView {

        /**
         * Displays the Array of category names in an Alert Dialogue
         * @param categoryNames The Array of category names to display
         */
        void createCategoriesAlertDialogue(String[] categoryNames);

        /**
         * Update the changes to the RecipeTag list.
         */
        void updateTagDisplay();
    }

    interface RecipeOverviewListener {
    }

    interface RecipeOverviewMyRecipeListener extends RecipeOverviewListener{
        void onSwapViewOverview();
        void onSync();
    }

    interface RecipeOverviewLikedRecipeListener extends RecipeOverviewListener {
    }
}
