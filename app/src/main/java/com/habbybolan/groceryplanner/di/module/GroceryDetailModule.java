package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsInteractor;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsPresenter;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsPresenterImpl;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation.IngredientLocationInteractor;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation.IngredientLocationInteractorImpl;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation.IngredientLocationPresenter;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation.IngredientLocationPresenterImpl;

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

    @Provides
    IngredientLocationInteractor provideIngredientLocationInteractor(DatabaseAccess databaseAccess) {
        return new IngredientLocationInteractorImpl(databaseAccess);
    }

    @Provides
    IngredientLocationPresenter provideIngredientLocationPresenter(IngredientLocationInteractor interactor) {
        return new IngredientLocationPresenterImpl(interactor);
    }
}
