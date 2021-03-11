package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewInteractorImpl implements RecipeOverviewInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }
    @Override
    public void updateRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        // todo:
    }

    @Override
    public void getCategory(Recipe recipe) {
        // todo:
    }

    @Override
    public void loadAllRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategories(recipeCategoriesObserved);
    }

    @Override
    public String[] getNamedOfRecipeCategories(ArrayList<RecipeCategory> recipeCategories) {
        String[] categoryNames = new String[recipeCategories.size()];
        for (int i = 0; i < categoryNames.length; i++) {
            categoryNames[i] = recipeCategories.get(i).getName();
        }
        return categoryNames;
    }

    @Override
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }
}
