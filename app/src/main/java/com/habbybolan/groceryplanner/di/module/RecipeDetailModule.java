package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.recipedetailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.recipe.recipedetailsactivity.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipedetailsactivity.RecipeDetailsPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.http.RestWebService;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {

    @Provides
    RecipeNutritionContract.Interactor provideRecipeNutritionInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeNutritionContract.Presenter provideRecipeNutritionPresenter(RecipeNutritionContract.Interactor interactor) {
        return new RecipeNutritionPresenterImpl(interactor);
    }

    @Provides
    RecipeOverviewContract.Interactor provideRecipeOverviewInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeOverviewContract.Presenter provideRecipeOverviewPresenter(RecipeOverviewContract.Interactor recipeOverviewInteractor) {
        return new RecipeOverviewPresenterImpl(recipeOverviewInteractor);
    }

    @Provides
    RecipeIngredientsContract.Interactor provideRecipeIngredientsInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsInteractorImpl(DatabaseAccess);
    }

    @Provides
    RecipeIngredientsContract.Presenter provideRecipeIngredientsPresenter(RecipeIngredientsContract.Interactor interactor) {
        return new RecipeIngredientsPresenterImpl(interactor);
    }

    @Provides
    RecipeInstructionsContract.Interactor provideRecipeInstructionsInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeInstructionsContract.Presenter provideRecipeInstructionsPresenter( RecipeInstructionsContract.Interactor interactor) {
        return new RecipeInstructionsPresenterImpl(interactor);
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
