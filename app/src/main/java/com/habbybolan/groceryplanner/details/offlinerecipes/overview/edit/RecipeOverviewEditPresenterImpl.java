package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbCallbackEmptyResponse;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewEditPresenterImpl
        extends RecipeOverviewPresenterImpl<RecipeOverviewContract.InteractorEdit, RecipeOverviewContract.OverviewEditView,
        MyRecipe>
        implements RecipeOverviewContract.PresenterEdit {

    private DbCallback<RecipeCategory> recipeCategoryDbCallback = new DbCallback<RecipeCategory>() {
        @Override
        public void onResponse(List<RecipeCategory> response) {
            // set the loaded recipe categories as loaded in
            loadedRecipeCategories.clear();
            loadedRecipeCategories.addAll(response);
        }
    };

    public RecipeOverviewEditPresenterImpl(RecipeOverviewContract.InteractorEdit interactor) {
        super(interactor);
    }

    @Override
    public void loadAllRecipeCategories() {
        // load the recipe categories only if it is not already loading
        try {
            interactor.loadAllRecipeCategories(recipeCategoryDbCallback);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RecipeCategory> getAllCategories() {
        return loadedRecipeCategories;
    }

    @Override
    public void displayRecipeCategories() {
        // display recipe categories if all loaded in
        String[] recipeCategoryNames = interactor.getNamedOfRecipeCategories(loadedRecipeCategories);
        view.createCategoriesAlertDialogue(recipeCategoryNames);
    }

    @Override
    public void deleteRecipeTag(RecipeTag recipeTag) {
        interactor.deleteRecipeTag(recipe, recipeTag, new DbCallbackEmptyResponse() {
            @Override
            public void onSuccess() {
                recipe.deleteTagByTitle(recipeTag.getTitle());
            }

            @Override
            public void onFailure(String message) {
                // TODO:
            }
        });
    }

    @Override
    public void addRecipeTag(String title) {
        interactor.addTag(recipe, title, new DbSingleCallbackWithFail<RecipeTag>() {
            @Override
            public void onResponse(RecipeTag response) {
                recipe.getRecipeTags().add(response);
                view.updateTagDisplay();
            }
            @Override
            public void onFail(String message) {
                // TODO:
            }
        });
    }

    @Override
    public void updateRecipe(String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        interactor.updateRecipe(recipe, name, servingSize, cookTime, prepTime, description, category);
    }
}
