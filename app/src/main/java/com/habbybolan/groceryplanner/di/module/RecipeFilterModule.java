package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.online.discover.searchfilter.RecipeFilterContract;
import com.habbybolan.groceryplanner.online.discover.searchfilter.RecipeFilterInteractorImpl;
import com.habbybolan.groceryplanner.online.discover.searchfilter.RecipeFilterPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeFilterModule {

    @Provides
    RecipeFilterContract.Presenter provideDiscoverPresenter(RecipeFilterContract.DiscoverInteractor interactor) {
        return new RecipeFilterPresenterImpl(interactor);
    }

    @Provides
    RecipeFilterContract.DiscoverInteractor provideDiscoverInteractor(HttpRecipe httpRecipe) {
        return new RecipeFilterInteractorImpl(httpRecipe);
    }
}
