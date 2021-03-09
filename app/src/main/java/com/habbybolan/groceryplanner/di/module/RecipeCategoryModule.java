package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryInteractor;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryInteractorImpl;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryPresenter;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeCategoryModule {

    @Provides
    RecipeCategoryInteractor provideRecipeCategoryInteractor(DatabaseAccess databaseAccess) {
        return new RecipeCategoryInteractorImpl(databaseAccess);
    }

    @Provides
    RecipeCategoryPresenter provideRecipeCategoryPresenter(RecipeCategoryInteractor interactor) {
        return new RecipeCategoryPresenterImpl(interactor);
    }
}
