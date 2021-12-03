package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeOverviewContract {

    interface Presenter<T extends OverviewView> {

        void destroy();

        void setView(T view);

        /**
         * Get the RecipeCategory at index position of the loaded RecipeCategories.
         * @param position  The position in the array of the RecipeCategory
         * @return          The RecipeCategory at position, null if the last position was selected (No Category)
         */
        RecipeCategory getRecipeCategory(int position);

        /**
         * Fetch the category from the database to display.
         * @param categoryId    The Id of the category to display
         */
        void fetchRecipeCategory(Long categoryId);
    }

    interface PresenterEdit extends Presenter<OverviewEditView> {

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
         * @param myRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(OfflineRecipe myRecipe, RecipeTag recipeTag);

        /**
         * Checks if the the name for the new RecipeTag is valid.
         * Adds the new tag if valid, otherwise send back error message.
         * @param title      Title of the new RecipeTag being added
         * @param myRecipe    Recipe the tag will be added to
         * @param recipeTags List of RecipeTag to add the new RecipeTag to
         */
        void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, OfflineRecipe myRecipe);

        /** Calls Interactor to update the recipe values changed. */
        void updateRecipe(OfflineRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly extends Presenter<OverviewView> {}

    interface Interactor {

        void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException;
    }

    interface InteractorEdit extends Interactor {

        void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

        /**
         * Converts the list of RecipeCategory to an array of their names
         * @param recipeCategories  loaded recipe categories
         * @return                  Array of the loaded RecipeCategory names to display
         */
        String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories);


        /**
         * Adds a tag to the recipe.
         * @param recipeTags List of RecipeTag to add the new RecipeTag to
         * @param title     title of the tag
         * @param myRecipe    recipe to place the tag into
         * @return          True if the recipe tag addition is valid, otherwise false
         */
        boolean addTag(List<RecipeTag> recipeTags, OfflineRecipe myRecipe, String title);

        /**
         * Delete the tag from the recipe
         * @param myRecipe    recipe holding the tag
         * @param recipeTag tag to delete from the recipe
         */
        void deleteRecipeTag(OfflineRecipe myRecipe, RecipeTag recipeTag);

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

    interface OverviewView {

        void loadingStarted();
        void loadingFailed(String message);

        /**
         * Displays the current RecipeCategory in the fragment
         * @param recipeCategory    RecipeCategory's name to display
         */
        void displayRecipeCategory(RecipeCategory recipeCategory);

        /**
         * Display the recipe tags.
         */
        void displayRecipeTags();
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
        OfflineRecipe getRecipe();
    }

    interface RecipeOverviewMyRecipeListener extends RecipeOverviewListener{
        void onSwapViewOverview();
        void onSync();
    }

    interface RecipeOverviewLikedRecipeListener extends RecipeOverviewListener {
    }
}
