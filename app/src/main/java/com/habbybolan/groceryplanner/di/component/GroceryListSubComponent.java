package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.GroceryListModule;
import com.habbybolan.groceryplanner.di.scope.ListScope;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListFragment;

import dagger.Subcomponent;

@ListScope
@Subcomponent(modules = {GroceryListModule.class})
public interface GroceryListSubComponent {

    void inject(GroceryListFragment fragment);
}
