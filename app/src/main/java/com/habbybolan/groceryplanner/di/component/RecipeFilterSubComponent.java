package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.RecipeFilterModule;
import com.habbybolan.groceryplanner.di.scope.OnlineScope;
import com.habbybolan.groceryplanner.online.discover.searchfilter.RecipeFilterFragment;

import dagger.Subcomponent;

@OnlineScope
@Subcomponent(modules = RecipeFilterModule.class)
public interface RecipeFilterSubComponent {

    void inject(RecipeFilterFragment fragment);
}
