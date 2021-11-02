package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.OnlineRecipeModule;
import com.habbybolan.groceryplanner.di.scope.OnlineScope;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListFragment;

import dagger.Subcomponent;

@OnlineScope
@Subcomponent(modules = {OnlineRecipeModule.class})
public interface OnlineSubComponent {

    void inject(OnlineRecipeListFragment fragment);
}
