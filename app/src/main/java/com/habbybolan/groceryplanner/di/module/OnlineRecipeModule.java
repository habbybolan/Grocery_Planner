package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.http.requests.HttpRecipe;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListContract;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListInteractorImpl;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListPresenterImpl;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview.OnlineRecipeEditOverviewContract;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview.OnlineRecipeEditOverviewInteractorImpl;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview.OnlineRecipeEditOverviewPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineRecipeModule {

    @Provides
    OnlineRecipeListContract.Presenter provideOnlineRecipeListPresenter(OnlineRecipeListContract.Interactor interactor) {
        return new OnlineRecipeListPresenterImpl(interactor);
    }

    @Provides
    OnlineRecipeListContract.Interactor provideOnlineRecipeListInteractor(HttpRecipe httpRecipe) {
        return new OnlineRecipeListInteractorImpl(httpRecipe);
    }

    @Provides
    OnlineRecipeEditOverviewContract.Presenter provideOnlineRecipeEditPresenter(OnlineRecipeEditOverviewContract.Interactor interactor) {
        return new OnlineRecipeEditOverviewPresenterImpl(interactor);
    }

    @Provides
    OnlineRecipeEditOverviewContract.Interactor provideOnlineRecipeEditInteractor() {
        return new OnlineRecipeEditOverviewInteractorImpl();
    }
}
