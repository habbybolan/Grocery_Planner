package com.habbybolan.groceryplanner.di.component;


import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.di.module.IngredientAddModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {IngredientAddModule.class})
public interface IngredientAddSubComponent {

    void inject(IngredientAddFragment fragment);
}
