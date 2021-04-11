package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {

    @Provides
    RecipeNutritionInteractor provideRecipeNutritionInteractor(DatabaseAccess databaseAccess) {
        return new RecipeNutritionInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeNutritionPresenter provideRecipeNutritionPresenter(RecipeNutritionInteractor recipeNutritionInteractor) {
        return new RecipeNutritionPresenterImpl(recipeNutritionInteractor);
    }

    @Provides
    RecipeOverviewInteractor provideRecipeOverviewInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeOverviewPresenter provideRecipeOverviewPresenter(RecipeOverviewInteractor recipeOverviewInteractor) {
        return new RecipeOverviewPresenterImpl(recipeOverviewInteractor);
    }

    @Provides
    RecipeIngredientsInteractor provideRecipeIngredientsInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeIngredientsInteractorImpl(DatabaseAccess);
    }

    @Provides
    RecipeIngredientsPresenter provideRecipeIngredientsPresenter(RecipeIngredientsInteractor recipeIngredientsInteractor) {
        return new RecipeIngredientsPresenterImpl(recipeIngredientsInteractor);
    }

    @Provides
    RecipeInstructionsInteractor provideRecipeInstructionsInteractor(DatabaseAccess databaseAccess) {
        return new RecipeInstructionsInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeInstructionsPresenter provideRecipeInstructionsPresenter(RecipeInstructionsInteractor recipeInstructionsInteractor) {
        return new RecipeInstructionsPresenterImpl(recipeInstructionsInteractor);
    }
}
