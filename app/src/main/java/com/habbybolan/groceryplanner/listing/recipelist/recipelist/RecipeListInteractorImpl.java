package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeListInteractorImpl implements RecipeListInteractor{

    private DatabaseAccess databaseAccess;

    public RecipeListInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void fetchRecipes(RecipeCategory recipeCategory, SortType sortType, DbCallback<Recipe> callback) throws ExecutionException, InterruptedException {
        // find the recipes associated with the id if there is any
        if (recipeCategory != null)
            databaseAccess.fetchRecipes(recipeCategory.getId(), sortType, callback);
        // otherwise, no category selected, get all recipes
        else
            databaseAccess.fetchRecipes(null, sortType, callback);
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
    public void addRecipe(Recipe recipe, Timestamp dateCreated) {
        databaseAccess.addRecipe(recipe, dateCreated);
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
    public void fetchCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        SortType sortType = new SortType();
        sortType.setSortType(SortType.SORT_ALPHABETICAL_ASC);
        databaseAccess.fetchRecipeCategories(callback, sortType);
    }

    @Override
    public void searchRecipes(RecipeCategory recipeCategory, String recipeSearch, DbCallback<Recipe> callback) throws ExecutionException, InterruptedException {
        if (recipeCategory == null)
            databaseAccess.searchRecipes(recipeSearch, callback);
        else
            databaseAccess.searchRecipesInCategory(recipeCategory.getId(), recipeSearch, callback);
    }
}
