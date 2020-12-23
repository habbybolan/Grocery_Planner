package com.habbybolan.groceryplanner.di.component;


import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.di.scope.ListScope;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListFragment;

import dagger.Subcomponent;

@ListScope
@Subcomponent(modules = RecipeListModule.class)
public interface RecipeListSubComponent {

    void inject(RecipeListFragment fragment);
}
