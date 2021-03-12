package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListInteractor;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListInteractorImpl;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListPresenter;
import com.habbybolan.groceryplanner.listing.recipelist.recipelist.RecipeListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeListModule {

    @Provides
    RecipeListInteractor provideRecipeListInteractor(DatabaseAccess databaseAccess) {
        return new RecipeListInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeListPresenter provideRecipeListPresenter(RecipeListInteractor interactor) {
        return new RecipeListPresenterImpl(interactor);
    }
}
