package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListInteractor;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListInteractorImpl;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListPresenter;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class IngredientListModule {

    @Provides
    IngredientListInteractor provideIngredientListInteractor(DatabaseAccess databaseAccess) {
        return new IngredientListInteractorImpl(databaseAccess);
    }

    @Provides
    IngredientListPresenter provideIngredientListPresenter(IngredientListInteractor interactor) {
        return new IngredientListPresenterImpl(interactor);
    }
}
