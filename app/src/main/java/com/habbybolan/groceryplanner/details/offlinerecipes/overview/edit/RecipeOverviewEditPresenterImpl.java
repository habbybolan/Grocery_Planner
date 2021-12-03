package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewEditPresenterImpl extends RecipeOverviewPresenterImpl<RecipeOverviewContract.InteractorEdit, RecipeOverviewContract.OverviewEditView> implements RecipeOverviewContract.PresenterEdit {

    private DbCallback<RecipeCategory> recipeCategoryDbCallback = new DbCallback<RecipeCategory>() {
        @Override
        public void onResponse(List<RecipeCategory> response) {
            // set the loaded recipe categories as loaded in
            loadedRecipeCategories.clear();
            loadedRecipeCategories.addAll(response);
            loadingRecipeCategories = false;
        }
    };

    public RecipeOverviewEditPresenterImpl(RecipeOverviewContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void loadAllRecipeCategories() {
        // load the recipe categories only if it is not already loading
        if (isCategoriesReady()) {
            view.loadingStarted();
            loadingRecipeCategories = true;
            try {
                interactor.loadAllRecipeCategories(recipeCategoryDbCallback);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                view.loadingFailed("failed retrieving data");
                loadingRecipeCategories = false;
            }
        } else {
            view.loadingFailed("recipe categories are being loaded");
        }
    }

    /**
     * Signals if the categories are being loaded in.
     * @return  True if the recipe categories are loaded in
     */
    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    @Override
    public List<RecipeCategory> getAllCategories() {
        return loadedRecipeCategories;
    }

    @Override
    public void displayRecipeCategories() {
        // display recipe categories if all loaded in
        if (isCategoriesReady()) {
            String[] recipeCategoryNames = interactor.getNamedOfRecipeCategories(loadedRecipeCategories);
            view.createCategoriesAlertDialogue(recipeCategoryNames);
        } else {
            // otherwise, recipe categories not loaded in yet
            view.loadingFailed("recipe categories are being loaded");
        }
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe recipe, RecipeTag recipeTag) {
        interactor.deleteRecipeTag(recipe, recipeTag);
    }

    @Override
    public void checkAddingRecipeTag(String title, List<RecipeTag> recipeTags, OfflineRecipe recipe) {
        // todo: make this a callable structure
        if (interactor.addTag(recipeTags, recipe, title)) {
            view.updateTagDisplay();
        } else {
            view.loadingFailed("Invalid Recipe tag name");
        }
    }

    @Override
    public void updateRecipe(OfflineRecipe recipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        interactor.updateRecipe(recipe, name, servingSize, cookTime, prepTime, description, category);
    }
}
