package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeCategoryInteractorImpl implements RecipeCategoryInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeCategoryInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserver) throws ExecutionException, InterruptedException {
       databaseAccess.fetchRecipeCategories(recipeCategoriesObserver);
    }

    @Override
    public void deleteRecipeCategory(RecipeCategory recipeCategory) {
        databaseAccess.deleteRecipeCategory(recipeCategory);
    }

    @Override
    public void deleteRecipeCategories(List<RecipeCategory> recipeCategories) {
        databaseAccess.deleteRecipeCategories(recipeCategories);
    }

    @Override
    public List<Recipe> searchRecipeCategories(String search) {
        // todo:
        return null;
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        databaseAccess.addRecipeCategory(recipeCategory);
    }
}
