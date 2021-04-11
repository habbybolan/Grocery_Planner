package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import androidx.databinding.ObservableArrayList;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListInteractorImpl implements RecipeListInteractor{

    private DatabaseAccess databaseAccess;

    public RecipeListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipes(RecipeCategory recipeCategory, ObservableArrayList<Recipe> recipesObserved) throws ExecutionException, InterruptedException {
        // find the recipes associated with the id if there is any
        if (recipeCategory != null)
            databaseAccess.fetchRecipes(recipeCategory.getId(), recipesObserved);
        // otherwise, no category selected, get all recipes
        else
            databaseAccess.fetchRecipes(null, recipesObserved);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe.getId());
    }

    @Override
    public void deleteRecipes(List<Recipe> recipes) {
        List<Long> recipeIds = new ArrayList<>();
        for (Recipe recipe : recipes) recipeIds.add(recipe.getId());
        databaseAccess.deleteRecipes(recipeIds);
    }

    @Override
    public List<Recipe> searchRecipes(String search) {
        // todo:
        return null;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        databaseAccess.addRecipe(recipe);
    }

    @Override
    public void addRecipesToCategory(ArrayList<Recipe> recipes, RecipeCategory category) {
        for (Recipe recipe : recipes) {
            recipe.setCategoryId(category.getId());
        }
        databaseAccess.updateRecipes(recipes);
    }

    @Override
    public void removeRecipesFromCategory(ArrayList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            recipe.setCategoryId(null);
        }
        databaseAccess.updateRecipes(recipes);
    }

    @Override
    public Long getCategoryId(RecipeCategory recipeCategory) {
        return recipeCategory == null ? null : recipeCategory.getId();
    }

    @Override
    public void fetchCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategories(recipeCategoriesObserved);
    }
}
