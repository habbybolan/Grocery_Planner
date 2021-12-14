package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe.DetailsLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe.RecipeDetailsLikedRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.DetailsMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.RecipeDetailsMyRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsLikedRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsMyRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsLikedRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsMyRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionInteractorEditImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionPresenterEditImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionLikedRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionMyRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListPresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewLikedRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewLikedRecipePresenterImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewMyRecipeInteractorImpl;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewMyRecipePresenterImpl;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.sync.SyncRecipeFromResponse;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {

    // Nutrition

    @Provides
    RecipeNutritionContract.InteractorMyRecipeReadOnly provideRecipeNutritionMyRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionMyRecipeInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeNutritionContract.InteractorLikedRecipeReadOnly provideRecipeNutritionLikedRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionLikedRecipeInteractorImpl(databaseAccess);
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
    RecipeNutritionContract.PresenterMyRecipe provideRecipeNutritionMyRecipePresenter(RecipeNutritionContract.InteractorMyRecipeReadOnly interactor) {
        return new RecipeNutritionMyRecipePresenterImpl(interactor);
    }
    @Provides
    RecipeNutritionContract.PresenterLikedRecipe provideRecipeNutritionLikedRecipePresenter(RecipeNutritionContract.InteractorLikedRecipeReadOnly interactor) {
        return new RecipeNutritionLikedRecipePresenterImpl(interactor);
    }

    // Overview

    @Provides
    RecipeOverviewContract.InteractorEdit provideRecipeOverviewEditInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewEditInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeOverviewContract.InteractorMyRecipeReadOnly provideRecipeOverviewMyRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewMyRecipeInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeOverviewContract.InteractorLikedRecipeReadOnly provideRecipeOverviewLikedRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewLikedRecipeInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeOverviewContract.PresenterEdit provideRecipeOverviewEditPresenter(RecipeOverviewContract.InteractorEdit recipeOverviewInteractor) {
        return new RecipeOverviewEditPresenterImpl(recipeOverviewInteractor);
    }
    @Provides
    RecipeOverviewContract.PresenterMyRecipe provideRecipeOverviewMyRecipePresenter(RecipeOverviewContract.InteractorMyRecipeReadOnly interactor) {
        return new RecipeOverviewMyRecipePresenterImpl(interactor);
    }
    @Provides
    RecipeOverviewContract.PresenterLikedRecipe provideRecipeOverviewLikedRecipePresenter(RecipeOverviewContract.InteractorLikedRecipeReadOnly interactor) {
        return new RecipeOverviewLikedRecipePresenterImpl(interactor);
    }

    // Ingredients

    @Provides
    RecipeIngredientsContract.InteractorMyRecipe provideRecipeIngredientsMyRecipeInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsMyRecipeInteractorImpl(DatabaseAccess);
    }
    @Provides
    RecipeIngredientsContract.InteractorLikedRecipe provideRecipeIngredientsLikedRecipeInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsLikedRecipeInteractorImpl(DatabaseAccess);
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
    RecipeIngredientsContract.PresenterMyRecipe provideRecipeIngredientsMyRecipePresenter(RecipeIngredientsContract.InteractorMyRecipe interactor) {
        return new RecipeIngredientsMyRecipePresenterImpl(interactor);
    }
    @Provides
    RecipeIngredientsContract.PresenterLikedRecipe provideRecipeIngredientsLikedRecipePresenter(RecipeIngredientsContract.InteractorLikedRecipe interactor) {
        return new RecipeIngredientsLikedRecipePresenterImpl(interactor);
    }

    // Instructions

    @Provides
    RecipeInstructionsContract.InteractorEdit provideRecipeInstructionsEditInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsEditInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeInstructionsContract.InteractorMyRecipe provideRecipeInstructionsMyRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsMyRecipeInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeInstructionsContract.InteractorLikedRecipe provideRecipeInstructionsLikedRecipeInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsLikedRecipeInteractorImpl(databaseAccess);
    }
    @Provides
    RecipeInstructionsContract.PresenterEdit provideRecipeInstructionsEditPresenter( RecipeInstructionsContract.InteractorEdit interactor) {
        return new RecipeInstructionsEditPresenterImpl(interactor);
    }
    @Provides
    RecipeInstructionsContract.PresenterMyRecipe provideRecipeInstructionsMyRecipePresenter( RecipeInstructionsContract.InteractorMyRecipe interactor) {
        return new RecipeInstructionsMyRecipePresenterImpl(interactor);
    }
    @Provides
    RecipeInstructionsContract.PresenterLikedRecipe provideRecipeInstructionsLikedRecipePresenter( RecipeInstructionsContract.InteractorLikedRecipe interactor) {
        return new RecipeInstructionsLikedRecipePresenterImpl(interactor);
    }



    @Provides
    AddRecipeToGroceryListContract.Presenter provideAddRecipeToGroceryListPresenter(AddRecipeToGroceryListContract.Interactor interactor) {
        return new AddRecipeToGroceryListPresenterImpl(interactor);
    }

    @Provides
    AddRecipeToGroceryListContract.Interactor provideAddRecipeToGroceryListInteractor(DatabaseAccess databaseAccess) {
        return new AddRecipeToGroceryListInteractorImpl(databaseAccess);
    }


    @Provides
    RecipeDetailsContract.PresenterMyRecipe provideRecipeDetailMyRecipePresenter(RecipeDetailsContract.InteractorMyRecipe interactor) {
        return new DetailsMyRecipePresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.PresenterLikedRecipe provideRecipeDetailLikedRecipePresenter(RecipeDetailsContract.InteractorLikedRecipe interactor) {
        return new DetailsLikedRecipePresenterImpl(interactor);
    }

    @Provides
    RecipeDetailsContract.InteractorMyRecipe provideMyRecipeDetailInteractor(DatabaseAccess databaseAccess, RestWebService restWebService, SyncRecipeFromResponse syncRecipes) {
        return new RecipeDetailsMyRecipeInteractorImpl(databaseAccess, restWebService, syncRecipes);
    }

    @Provides
    RecipeDetailsContract.InteractorLikedRecipe provideLikedRecipeDetailInteractor(DatabaseAccess databaseAccess, RestWebService restWebService) {
        return new RecipeDetailsLikedRecipeInteractorImpl(databaseAccess, restWebService);
    }
}
