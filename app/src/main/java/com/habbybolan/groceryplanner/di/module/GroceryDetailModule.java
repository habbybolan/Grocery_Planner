package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailsInteractor;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailsInteractorImpl;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailsPresenter;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class GroceryDetailModule {

    @Provides
    GroceryDetailsInteractor provideGroceryDetailInteractor(DatabaseAccess DatabaseAccess) {
        return new GroceryDetailsInteractorImpl(DatabaseAccess);
    }

    @Provides
    GroceryDetailsPresenter provideGroceryDetailsPresenter(GroceryDetailsInteractor groceryDetailsInteractor) {
        return new GroceryDetailsPresenterImpl(groceryDetailsInteractor);
    }
}
