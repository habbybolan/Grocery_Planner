package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface RecipeListPresenter {

    void destroy();
    void deleteRecipe(OfflineRecipe offlineRecipe);
    void deleteRecipes(List<OfflineRecipe> offlineRecipes);
    void addRecipe(OfflineRecipe offlineRecipe, Timestamp dateCreated);

    void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipe, RecipeCategory category);
    void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes);

    void setView(RecipeListView view);
    void createRecipeList();

    void fetchCategories() ;
    ArrayList<RecipeCategory> getLoadedRecipeCategories();

    /**
     * Search for the recipes with name recipeSearch.
     * @param recipeSearch   recipe to search for
     */
    void searchRecipes(String recipeSearch);
}
