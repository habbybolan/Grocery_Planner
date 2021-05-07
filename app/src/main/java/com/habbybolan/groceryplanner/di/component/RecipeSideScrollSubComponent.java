package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollFragment;
import com.habbybolan.groceryplanner.di.module.RecipeSideScrollModule;
import com.habbybolan.groceryplanner.di.scope.ListScope;

import dagger.Subcomponent;

@ListScope
@Subcomponent(modules = RecipeSideScrollModule.class)
public interface RecipeSideScrollSubComponent {

    void inject(RecipeSideScrollFragment fragment);
}
