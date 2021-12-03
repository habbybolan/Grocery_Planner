package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
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
    public boolean addTag(List<RecipeTag> recipeTags, OfflineRecipe myRecipe, String title) {
        String reformattedTitle = RecipeTag.reformatTagTitle(title);
        RecipeTag recipeTag = new RecipeTag(reformattedTitle);
        if (RecipeTag.isTagTitleValid(title) && !recipeTags.contains(recipeTag)) {
            databaseAccess.addRecipeTag(myRecipe.getId(), reformattedTitle);
            recipeTags.add(new RecipeTag(reformattedTitle));
            return true;
        }
        return false;
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe myRecipe, RecipeTag recipeTag) {
        databaseAccess.deleteRecipeTagFromBridge(myRecipe.getId(), recipeTag);
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
