package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.DetailsLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.DetailsMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsReadOnlyPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsReadOnlyPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionInteractorEditImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionPresenterEditImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionPresenterReadOnlyImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewReadOnlyPresenterImpl;
import com.habbybolan.groceryplanner.http.RestWebService;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {

    // Nutrition

    @Provides
    RecipeNutritionContract.Interactor provideRecipeNutritionReadOnlyInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeNutritionContract.InteractorEdit provideRecipeNutritionEditInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionInteractorEditImpl(databaseAccess);
    }
    @Provides
    RecipeNutritionContract.PresenterEdit provideRecipeNutritionEditPresenter(RecipeNutritionContract.InteractorEdit interactor) {
        return new RecipeNutritionPresenterEditImpl(interactor);
    }
    @Provides
    RecipeNutritionContract.PresenterReadOnly provideRecipeNutritionReadOnlyPresenter(RecipeNutritionContract.Interactor interactor) {
        return new RecipeNutritionPresenterReadOnlyImpl(interactor);
    }

    // Overview

    @Provides
    RecipeOverviewContract.InteractorEdit provideRecipeOverviewEditInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewEditInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeOverviewContract.Interactor provideRecipeOverviewReadOnlyInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeOverviewContract.PresenterEdit provideRecipeOverviewEditPresenter(RecipeOverviewContract.InteractorEdit recipeOverviewInteractor) {
        return new RecipeOverviewEditPresenterImpl(recipeOverviewInteractor);
    }
    @Provides
    RecipeOverviewContract.PresenterReadOnly provideRecipeOverviewReadOnlyPresenter(RecipeOverviewContract.Interactor recipeOverviewInteractor) {
        return new RecipeOverviewReadOnlyPresenterImpl(recipeOverviewInteractor);
    }

    // Ingredients

    @Provides
    RecipeIngredientsContract.Interactor provideRecipeIngredientsInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsInteractorImpl(DatabaseAccess);
    }
    @Provides
    RecipeIngredientsContract.InteractorEdit provideRecipeIngredientsEditInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsEditInteractorImpl(DatabaseAccess);
    }
    @Provides
    RecipeIngredientsContract.PresenterEdit provideRecipeIngredientsEditPresenter(RecipeIngredientsContract.InteractorEdit interactor) {
        return new RecipeIngredientsEditPresenterImpl(interactor);
    }
    @Provides
    RecipeIngredientsContract.PresenterReadOnly provideRecipeIngredientsPresenter(RecipeIngredientsContract.Interactor interactor) {
        return new RecipeIngredientsReadOnlyPresenterImpl(interactor);
    }

    // Instructions

    @Provides
    RecipeInstructionsContract.InteractorEdit provideRecipeInstructionsEditInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsEditInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeInstructionsContract.Interactor provideRecipeInstructionsInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeInstructionsContract.PresenterEdit provideRecipeInstructionsEditPresenter( RecipeInstructionsContract.InteractorEdit interactor) {
        return new RecipeInstructionsEditPresenterImpl(interactor);
    }
    @Provides
    RecipeInstructionsContract.PresenterReadOnly provideRecipeInstructionsReadOnlyPresenter( RecipeInstructionsContract.Interactor interactor) {
        return new RecipeInstructionsReadOnlyPresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.PresenterMyRecipe provideRecipeDetailLikedRecipePresenter(RecipeDetailsContract.Interactor interactor) {
        return new DetailsMyRecipePresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.PresenterLikedRecipe provideRecipeDetailMyRecipePresenter(RecipeDetailsContract.Interactor interactor) {
        return new DetailsLikedRecipePresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.Interactor provideRecipeDetailInteractor(DatabaseAccess databaseAccess, RestWebService restWebService) {
        return new RecipeDetailsInteractorImpl(databaseAccess, restWebService);
    }
}
