package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollInteractor;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollInteractorImpl;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollPresenter;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollPresenterImpl;
import com.habbybolan.groceryplanner.http.RestWebService;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeSideScrollModule {

    @Provides
    RecipeSideScrollInteractor provideSideScrollInteractor(RestWebService restWebService) {
        return new RecipeSideScrollInteractorImpl(restWebService);
    }

    @Provides
    RecipeSideScrollPresenter provideSideScrollPresenter(RecipeSideScrollInteractor interactor) {
        return new RecipeSideScrollPresenterImpl(interactor);
    }
}
