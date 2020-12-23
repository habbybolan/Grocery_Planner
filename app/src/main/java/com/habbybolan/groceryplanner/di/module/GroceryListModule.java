package com.habbybolan.groceryplanner.di.module;


import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListInteractor;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListInteractorImpl;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListPresenter;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class GroceryListModule {

    @Provides
    GroceryListInteractor provideGroceryListInteractor(DatabaseAccess databaseAccess) {
        return new GroceryListInteractorImpl(databaseAccess);
    }

    @Provides
    GroceryListPresenter provideGroceryListPresenter(GroceryListInteractor interactor) {
        return new GroceryListPresenterImpl(interactor);
    }
}
