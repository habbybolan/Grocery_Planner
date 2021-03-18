package com.habbybolan.groceryplanner.di.component;


import com.habbybolan.groceryplanner.di.module.AppModule;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.GroceryListModule;
import com.habbybolan.groceryplanner.di.module.IngredientAddModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.RecipeCategoryModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.di.module.RoomModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    GroceryListSubComponent groceryListSubComponent(GroceryListModule groceryListModule);
    GroceryDetailSubComponent groceryDetailSubComponent(GroceryDetailModule groceryDetailModule);

    RecipeCategorySubComponent recipeCategorySubComponent(RecipeCategoryModule recipeCategoryModule);
    RecipeListSubComponent recipeListSubComponent(RecipeListModule recipeListModule);
    RecipeDetailSubComponent recipeDetailSubComponent(RecipeDetailModule recipeDetailModule);

    IngredientEditSubComponent ingredientEditSubComponent(IngredientEditModule ingredientEditModule);
    IngredientAddSubComponent ingredientAddSubComponent(IngredientAddModule ingredientAddModule);
}
