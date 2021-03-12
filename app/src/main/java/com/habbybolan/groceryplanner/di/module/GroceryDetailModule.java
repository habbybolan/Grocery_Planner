package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsInteractor;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsPresenter;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class GroceryDetailModule {

    @Provides
    GroceryIngredientsInteractor provideGroceryIngredientsInteractor(DatabaseAccess DatabaseAccess) {
        return new GroceryIngredientsInteractorImpl(DatabaseAccess);
    }

    @Provides
    GroceryIngredientsPresenter provideGroceryIngredientsPresenter(GroceryIngredientsInteractor groceryIngredientsInteractor) {
        return new GroceryIngredientsPresenterImpl(groceryIngredientsInteractor);
    }
}
