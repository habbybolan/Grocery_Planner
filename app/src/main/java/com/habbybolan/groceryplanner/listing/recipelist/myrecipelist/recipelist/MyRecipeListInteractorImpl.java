package com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRecipeListInteractorImpl implements MyRecipeListContract.Interactor{

    private DatabaseAccess databaseAccess;

    public MyRecipeListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipes(RecipeCategory recipeCategory, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        // find the recipes associated with the category id if there is any
        if (recipeCategory != null) {
            databaseAccess.fetchMyRecipes(recipeCategory.getId(), sortType, callback);
            // otherwise, no category selected, get all
        } else databaseAccess.fetchMyRecipes(null, sortType, callback);
    }

    @Override
    public void deleteRecipe(OfflineRecipe offlineRecipe) {
        databaseAccess.deleteRecipe(offlineRecipe.getId());
    }

    @Override
    public void deleteRecipes(List<OfflineRecipe> offlineRecipes) {
        List<Long> recipeIds = new ArrayList<>();
        for (OfflineRecipe offlineRecipe : offlineRecipes) recipeIds.add(offlineRecipe.getId());
        databaseAccess.deleteRecipes(recipeIds);
    }

    @Override
    public void addRecipe(OfflineRecipe offlineRecipe, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        offlineRecipe.setDateCreated(new Timestamp(System.currentTimeMillis()/1000));
        databaseAccess.insertMyRecipe(offlineRecipe, callback);
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
            databaseAccess.searchMyRecipes(recipeSearch, callback);
        else
            databaseAccess.searchMyRecipesInCategory(recipeCategory.getId(), recipeSearch, callback);
    }
}
