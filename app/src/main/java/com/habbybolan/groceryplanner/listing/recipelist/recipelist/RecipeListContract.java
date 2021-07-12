package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeListContract {

    interface State {

        /**
         * Change the offset to represent going to next page.
         */
        List<Recipe> gotoNextPage() throws IllegalStateException;

        /**
         * @return  true if not at the end of the list and can display more on another page.
         */
        boolean canGotoNextPage();

        /**
         * Change the offset to represent going back to the previous page.
         */
        List<Recipe> gotoPreviousPage() throws IllegalStateException;

        /**
         * @return  True if user can go back a page, false otherwise.
         */
        boolean canGotoPreviousPage();

        /**
         * Sets the Recipe list.
         * @param recipes   List of recipes to store.
         */
        void setRecipeList(List<Recipe> recipes);

        int getOffset();
        int getSize();
        RecipeCategory getRecipeCategory();
        List<Recipe> getRecipes();
    }

    interface Presenter {

        void destroy();
        void deleteRecipe(OfflineRecipe offlineRecipe);
        void deleteRecipes(List<OfflineRecipe> offlineRecipes);
        void addRecipe(OfflineRecipe offlineRecipe);

        void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipe, RecipeCategory category);
        void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes);

        void setView(View view);
        void setState(State state);
        void createRecipeList();

        void fetchCategories() ;
        ArrayList<RecipeCategory> getLoadedRecipeCategories();

        /**
         * Search for the recipes with name recipeSearch.
         * @param recipeSearch   recipe to search for
         */
        void searchRecipes(String recipeSearch);
    }

    interface Interactor {

        void fetchRecipes(RecipeCategory recipeCategory, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
        void deleteRecipe(OfflineRecipe offlineRecipe);
        void deleteRecipes(List<OfflineRecipe> offlineRecipes);
        void addRecipe(OfflineRecipe offlineRecipe);
        void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipes, RecipeCategory category);
        void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes);

        /**
         * Get the ID of the recipe category.
         * @param recipeCategory    Category holding the ID
         * @return                  Recipe SQL category ID. NULL if recipeCategory is null
         */
        Long getCategoryId(RecipeCategory recipeCategory);
        void fetchCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException;

        /**
         * Search for the recipes with name recipeSearch.
         * @param recipeSearch   recipe to search for
         * @param recipeCategory category that is currently showing the recipes. NUll if no category.
         * @param callback      callback to update the list of recipes showing
         */
        void searchRecipes(RecipeCategory recipeCategory, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    }

    interface View extends ListViewInterface<OfflineRecipe> {
    }
}
