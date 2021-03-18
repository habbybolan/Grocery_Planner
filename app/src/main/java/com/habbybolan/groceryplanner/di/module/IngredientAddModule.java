package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddInteractor;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddInteractorImpl;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddPresenter;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class IngredientAddModule {

    @Provides
    IngredientAddInteractor provideIngredientAddInteractor(DatabaseAccess DatabaseAccess) {
        return new IngredientAddInteractorImpl(DatabaseAccess);
    }

    @Provides
    IngredientAddPresenter provideIngredientAddPresenter(IngredientAddInteractor ingredientAddInteractor) {
        return new IngredientAddPresenterImpl(ingredientAddInteractor);
    }
}
