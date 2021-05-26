package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeListInteractor {

    void fetchRecipes(RecipeCategory recipeCategory, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException;
    void deleteRecipe(OfflineRecipe offlineRecipe);
    void deleteRecipes(List<OfflineRecipe> offlineRecipes);
    void addRecipe(OfflineRecipe offlineRecipe, Timestamp dateCreated);
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
