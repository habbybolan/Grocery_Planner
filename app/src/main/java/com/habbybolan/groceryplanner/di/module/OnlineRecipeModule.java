package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListContract;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListInteractorImpl;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineRecipeModule {

    @Provides
    OnlineRecipeListContract.Presenter provideOnlineRecipeListPresenter(OnlineRecipeListContract.Interactor interactor) {
        return new OnlineRecipeListPresenterImpl(interactor);
    }

    @Provides
    OnlineRecipeListContract.Interactor provideOnlineRecipeListInteractor(RestWebService restWebService) {
        return new OnlineRecipeListInteractorImpl(restWebService);
    }
}
