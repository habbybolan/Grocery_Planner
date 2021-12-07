package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbCallbackDelete;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewEditInteractorImpl extends RecipeOverviewInteractorImpl implements RecipeOverviewContract.InteractorEdit {

    public RecipeOverviewEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        SortType sortType = new SortType();
        sortType.setSortType(SortType.SORT_ALPHABETICAL_ASC);
        databaseAccess.fetchRecipeCategories(callback, sortType);
    }



    @Override
    public String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories) {
        String[] categoryNames = new String[recipeCategories.size()+1];
        for (int i = 0; i < recipeCategories.size(); i++) {
            categoryNames[i] = recipeCategories.get(i).getName();
        }
        categoryNames[recipeCategories.size()] = "No Category";
        return categoryNames;
    }

    @Override
    public void addTag(OfflineRecipe myRecipe, String title, DbSingleCallbackWithFail<RecipeTag> callback) {
        String reformattedTitle = RecipeTag.reformatTagTitle(title);
        if (RecipeTag.isTagTitleValid(title)) {
            databaseAccess.insertTagIntoRecipe(myRecipe.getId(), new RecipeTag(reformattedTitle), callback);
        } else {
            callback.onFail("invalid tag name");
        }
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe myRecipe, RecipeTag recipeTag, DbCallbackDelete callback) {
        databaseAccess.deleteRecipeTagFromBridge(myRecipe.getId(), recipeTag.getId(), callback);
    }

    @Override
    public void updateRecipe(OfflineRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        myRecipe.setName(name);
        myRecipe.setServingSize(servingSize.length() > 0 ? Integer.parseInt(servingSize) : 0);
        myRecipe.setCookTime(cookTime.length() > 0 ? Integer.parseInt(cookTime) : 0);
        myRecipe.setPrepTime(prepTime.length() > 0 ? Integer.parseInt(prepTime) : 0);
        myRecipe.setDescription(description);
        if (category != null) {
            myRecipe.setCategoryId(category.getId());
        } else {
            // otherwise, the 'No Category' was selected
            myRecipe.setCategoryId(null);
        }
        databaseAccess.updateRecipe(myRecipe);
    }
}
