package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit.RecipeIngredientsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit.RecipeIngredientsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly.RecipeIngredientsReadOnlyPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.edit.RecipeInstructionsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.edit.RecipeInstructionsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.readonly.RecipeInstructionsReadOnlyPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionInteractorEditImpl;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionPresenterEditImpl;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly.RecipeNutritionPresenterReadOnlyImpl;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.overview.edit.RecipeOverviewEditInteractorImpl;
import com.habbybolan.groceryplanner.details.myrecipe.overview.edit.RecipeOverviewEditPresenterImpl;
import com.habbybolan.groceryplanner.details.myrecipe.overview.readonly.RecipeOverviewReadOnlyPresenterImpl;
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
    RecipeDetailsContract.Presenter provideRecipeDetailPresenter(RecipeDetailsContract.Interactor interactor) {
        return new RecipeDetailsPresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.Interactor provideRecipeDetailInteractor(DatabaseAccess databaseAccess, RestWebService restWebService) {
        return new RecipeDetailsInteractorImpl(databaseAccess, restWebService);
    }
}
