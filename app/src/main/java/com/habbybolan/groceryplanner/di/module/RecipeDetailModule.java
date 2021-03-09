package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.recipedetails.RecipeDetailInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipedetails.RecipeDetailInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipedetails.RecipeDetailPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipedetails.RecipeDetailPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewPresenterImpl;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepInteractor;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepInteractorImpl;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepPresenter;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {

    @Provides
    RecipeOverviewInteractor provideRecipeOverviewInteractor(DatabaseAccess databaseAccess) {
        return new RecipeOverviewInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeOverviewPresenter provideRecipeOverviewPresenter(RecipeOverviewInteractor recipeOverviewInteractor) {
        return new RecipeOverviewPresenterImpl(recipeOverviewInteractor);
    }

    @Provides
    RecipeDetailInteractor provideRecipeDetailInteractor(DatabaseAccess DatabaseAccess) {
        return new RecipeDetailInteractorImpl(DatabaseAccess);
    }

    @Provides
    RecipeDetailPresenter provideRecipeDetailPresenter(RecipeDetailInteractor recipeDetailInteractor) {
        return new RecipeDetailPresenterImpl(recipeDetailInteractor);
    }

    @Provides
    RecipeStepInteractor provideRecipeStepInteractor(DatabaseAccess databaseAccess) {
        return new RecipeStepInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeStepPresenter provideRecipeStepPresenter(RecipeStepInteractor recipeStepInteractor) {
        return new RecipeStepPresenterImpl(recipeStepInteractor);
    }
}
