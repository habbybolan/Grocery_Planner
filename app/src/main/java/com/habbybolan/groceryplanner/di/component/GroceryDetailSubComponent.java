package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsFragment;
import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {GroceryDetailModule.class, IngredientEditModule.class})
public interface GroceryDetailSubComponent {

    void inject(GroceryIngredientsFragment fragment);
    void inject(IngredientEditFragment fragment);
}
