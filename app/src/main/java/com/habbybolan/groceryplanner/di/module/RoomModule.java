package com.habbybolan.groceryplanner.di.module;

import android.app.Application;

import androidx.room.Room;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.database.DatabaseAccessImpl;
import com.habbybolan.groceryplanner.database.LocalDatabase;
import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.NutritionDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.dao.RecipeTagDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private LocalDatabase localDatabase;

    public RoomModule(Application application) {
        localDatabase = Room.databaseBuilder(application, LocalDatabase.class, "grocery_database")
                .addCallback(LocalDatabase.createCallback())
                .build();
    }

    @Singleton
    @Provides
    LocalDatabase provideRoomDatabase() {
        return localDatabase;
    }

    @Singleton
    @Provides
    GroceryDao providesGroceryDao(LocalDatabase localDatabase) {
        return localDatabase.getGroceryDao();
    }

    @Singleton
    @Provides
    RecipeDao providesRecipeDao(LocalDatabase localDatabase) {
        return localDatabase.getRecipeDao();
    }

    @Singleton
    @Provides
    RecipeTagDao providesRecipeTagDao(LocalDatabase localDatabase) {
        return localDatabase.getRecipeTagDao();
    }

    @Singleton
    @Provides
    NutritionDao providesNutritionDao(LocalDatabase localDatabase) {
        return localDatabase.getNutritionDao();
    }

    @Singleton
    @Provides
    IngredientDao provideIngredientDao(LocalDatabase localDatabase) {
        return localDatabase.getIngredientDao();
    }

    @Singleton
    @Provides
    DatabaseAccess provideDatabaseAccess(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao, RecipeTagDao recipeTagDao, NutritionDao nutritionDao) {
        return new DatabaseAccessImpl(groceryDao, recipeDao, ingredientDao, recipeTagDao, nutritionDao);
    }

}
