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
        // todo:
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
        // todo:
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<OfflineRecipe> offlineRecipes) {
        // todo:
    }

    @Override
    public Long getCategoryId(RecipeCategory recipeCategory) {
        // todo:
        return null;
    }

    @Override
    public void fetchCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        // todo:
    }

    @Override
    public void searchRecipes(RecipeCategory recipeCategory, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        // todo:
    }
}
