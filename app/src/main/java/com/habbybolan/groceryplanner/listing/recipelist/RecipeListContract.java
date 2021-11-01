package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;

public interface RecipeListContract {

    interface Presenter {
        void destroy();

        void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipe, RecipeCategory category);
        void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes);
        void setState(RecipeListState state);
        void createRecipeList();

        void fetchCategories() ;
        ArrayList<RecipeCategory> getLoadedRecipeCategories();

        /**
         * Search for the recipes with name recipeSearch.
         * @param recipeSearch   recipe to search for
         */
        void searchRecipes(String recipeSearch);
    }
}
