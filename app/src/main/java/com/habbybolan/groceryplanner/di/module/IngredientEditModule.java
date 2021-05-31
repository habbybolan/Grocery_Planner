package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditContract;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditInteractorImpl;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class IngredientEditModule {

    @Provides
    IngredientEditContract.Interactor provideIngredientEditInteractor(DatabaseAccess DatabaseAccess) {
        return new IngredientEditInteractorImpl(DatabaseAccess);
    }

    @Provides
    IngredientEditContract.Presenter provideIngredientEditPresenter(IngredientEditContract.Interactor ingredientEditInteractor) {
        return new IngredientEditPresenterImpl(ingredientEditInteractor);
    }
}
