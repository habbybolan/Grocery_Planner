package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.IngredientListModule;
import com.habbybolan.groceryplanner.di.scope.ListScope;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListFragment;

import dagger.Subcomponent;

@ListScope
@Subcomponent(modules = IngredientListModule.class)
public interface IngredientListSubComponent {

    void inject(IngredientListFragment fragment);
}
