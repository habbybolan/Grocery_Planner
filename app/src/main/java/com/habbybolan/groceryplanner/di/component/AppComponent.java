package com.habbybolan.groceryplanner.di.component;


import com.habbybolan.groceryplanner.di.module.AppModule;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.module.GroceryListModule;
import com.habbybolan.groceryplanner.di.module.HttpRequestModule;
import com.habbybolan.groceryplanner.di.module.IngredientAddModule;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.IngredientListModule;
import com.habbybolan.groceryplanner.di.module.LoginModule;
import com.habbybolan.groceryplanner.di.module.RecipeCategoryModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.di.module.RecipeSideScrollModule;
import com.habbybolan.groceryplanner.di.module.RoomModule;
import com.habbybolan.groceryplanner.di.module.SignUpModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class, HttpRequestModule.class})
public interface AppComponent {

    GroceryListSubComponent groceryListSubComponent(GroceryListModule groceryListModule);
    GroceryDetailSubComponent groceryDetailSubComponent(GroceryDetailModule groceryDetailModule);

    RecipeCategorySubComponent recipeCategorySubComponent(RecipeCategoryModule recipeCategoryModule);
    RecipeListSubComponent recipeListSubComponent(RecipeListModule recipeListModule);
    RecipeDetailSubComponent recipeDetailSubComponent(RecipeDetailModule recipeDetailModule);

    IngredientEditSubComponent ingredientEditSubComponent(IngredientEditModule ingredientEditModule);
    IngredientAddSubComponent ingredientAddSubComponent(IngredientAddModule ingredientAddModule);

    LoginSubComponent loginSubComponent(SignUpModule signUpModule, LoginModule loginModule);

    IngredientListSubComponent ingredientListSubComponent(IngredientListModule ingredientListModule);

    RecipeSideScrollSubComponent recipeSideScrollSubComponent(RecipeSideScrollModule recipeSideScrollModule);
}
