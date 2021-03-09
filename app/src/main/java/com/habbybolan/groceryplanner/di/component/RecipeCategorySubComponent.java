package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.RecipeCategoryModule;
import com.habbybolan.groceryplanner.di.scope.ListScope;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryFragment;

import dagger.Subcomponent;

@ListScope
@Subcomponent(modules = RecipeCategoryModule.class)
public interface RecipeCategorySubComponent {

    void inject(RecipeCategoryFragment fragment);
}
