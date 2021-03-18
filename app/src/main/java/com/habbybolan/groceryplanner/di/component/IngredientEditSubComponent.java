package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {IngredientEditModule.class})
public interface IngredientEditSubComponent {

    void inject(IngredientEditFragment fragment);
}
