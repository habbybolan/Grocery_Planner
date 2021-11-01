package com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LikedRecipeListInteractorImpl implements LikedRecipeListContract.Interactor {

    private DatabaseAccess databaseAccess;

    public LikedRecipeListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipes(RecipeCategory recipeCategory, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        // find the recipes associated with the category id if there is any
        if (recipeCategory != null) {
            databaseAccess.fetchLikedRecipes(recipeCategory.getId(), sortType, callback);
            // otherwise, no category selected, get all
        } else databaseAccess.fetchLikedRecipes(null, sortType, callback);
    }

    @Override
    public void unlikeRecipe(OfflineRecipe offlineRecipe) {
        // todo:
    }

    @Override
    public void unlikeRecipes(List<OfflineRecipe> offlineRecipes) {
        // todo:
    }

    @Override
    public void addRecipesToCategory(ArrayList<OfflineRecipe> offlineRecipes, RecipeCategory category) {
        for (OfflineRecipe offlineRecipe : offlineRecipes) {
            offlineRecipe.setCategoryId(category.getId());
        }
        databaseAccess.updateRecipes(offlineRecipes);
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes) {
        for (OfflineRecipe offlineRecipe : offlineRecipes) {
            offlineRecipe.setCategoryId(null);
        }
        databaseAccess.updateRecipes(offlineRecipes);
    }

    @Override
    public Long getCategoryId(RecipeCategory recipeCategory) {
        return recipeCategory == null ? null : recipeCategory.getId();
    }

    @Override
    public void fetchCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        SortType sortType = new SortType();
        sortType.setSortType(SortType.SORT_ALPHABETICAL_ASC);
        databaseAccess.fetchRecipeCategories(callback, sortType);
    }

    @Override
    public void searchRecipes(RecipeCategory recipeCategory, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        if (recipeCategory == null)
            databaseAccess.searchLikedRecipes(recipeSearch, callback);
        else
            databaseAccess.searchLikedRecipesInCategory(recipeCategory.getId(), recipeSearch, callback);
    }
}
