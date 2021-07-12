package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListContract;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListInteractorImpl;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeListModule {

    @Provides
    RecipeListContract.Interactor provideRecipeListInteractor(DatabaseAccess databaseAccess) {
        return new RecipeListInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeListContract.Presenter provideRecipeListPresenter(RecipeListContract.Interactor interactor) {
        return new RecipeListPresenterImpl(interactor);
    }
}
