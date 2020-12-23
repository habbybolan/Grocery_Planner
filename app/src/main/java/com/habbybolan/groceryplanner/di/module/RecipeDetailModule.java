package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipedetails.RecipeDetailInteractor;
import com.habbybolan.groceryplanner.details.recipedetails.RecipeDetailInteractorImpl;
import com.habbybolan.groceryplanner.details.recipedetails.RecipeDetailPresenter;
import com.habbybolan.groceryplanner.details.recipedetails.RecipeDetailPresenterImpl;
import com.habbybolan.groceryplanner.details.recipesteps.RecipeStepInteractor;
import com.habbybolan.groceryplanner.details.recipesteps.RecipeStepInteractorImpl;
import com.habbybolan.groceryplanner.details.recipesteps.RecipeStepPresenter;
import com.habbybolan.groceryplanner.details.recipesteps.RecipeStepPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeDetailModule {


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
