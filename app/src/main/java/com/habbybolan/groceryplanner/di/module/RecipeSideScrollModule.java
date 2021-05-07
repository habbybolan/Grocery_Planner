package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollInteractor;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollInteractorImpl;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollPresenter;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollPresenterImpl;
import com.habbybolan.groceryplanner.http.requests.HttpRecipe;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeSideScrollModule {

    @Provides
    RecipeSideScrollInteractor provideSideScrollInteractor(HttpRecipe httpRecipe) {
        return new RecipeSideScrollInteractorImpl(httpRecipe);
    }

    @Provides
    RecipeSideScrollPresenter provideSideScrollPresenter(RecipeSideScrollInteractor interactor) {
        return new RecipeSideScrollPresenterImpl(interactor);
    }
}
