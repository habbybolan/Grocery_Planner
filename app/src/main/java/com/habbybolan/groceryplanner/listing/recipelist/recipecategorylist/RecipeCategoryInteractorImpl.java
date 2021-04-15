package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
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
    public List<Recipe> searchRecipeCategories(String search) {
        // todo:
        return null;
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        databaseAccess.addRecipeCategory(recipeCategory);
    }
}
