package com.habbybolan.groceryplanner.di.module;


import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListInteractor;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListInteractorImpl;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListPresenter;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListPresenterImpl;

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
