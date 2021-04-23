package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeCategoryInteractorImpl implements RecipeCategoryInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeCategoryInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException {
       databaseAccess.fetchRecipeCategories(callback, sortType);
    }

    @Override
    public void deleteRecipeCategory(RecipeCategory recipeCategory) {
        databaseAccess.deleteRecipeCategory(recipeCategory.getId());
    }

    @Override
    public void deleteRecipeCategories(List<RecipeCategory> recipeCategories) {
        // convert into a list of recipe category ids to delete
        List<Long> categoryIds = new ArrayList<>();
        for (RecipeCategory recipeCategory : recipeCategories) categoryIds.add(recipeCategory.getId());
        databaseAccess.deleteRecipeCategories(categoryIds);
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        databaseAccess.addRecipeCategory(recipeCategory);
    }

    @Override
    public void searchRecipeCategories(String categorySearch, DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        databaseAccess.searchRecipeCategories(categorySearch, callback);
    }
}
