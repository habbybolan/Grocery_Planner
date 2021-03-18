package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditInteractor;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditInteractorImpl;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditPresenter;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class IngredientEditModule {

    @Provides
    IngredientEditInteractor provideIngredientEditInteractor(DatabaseAccess DatabaseAccess) {
        return new IngredientEditInteractorImpl(DatabaseAccess);
    }

    @Provides
    IngredientEditPresenter provideIngredientEditPresenter(IngredientEditInteractor ingredientEditInteractor) {
        return new IngredientEditPresenterImpl(ingredientEditInteractor);
    }
}
