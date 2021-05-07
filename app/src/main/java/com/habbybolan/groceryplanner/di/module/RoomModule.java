package com.habbybolan.groceryplanner.di.module;

import android.app.Application;

import androidx.room.Room;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.database.DatabaseAccessImpl;
import com.habbybolan.groceryplanner.database.LocalDatabase;
import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;

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
    GroceryDao providesGroceryIngredientDao(LocalDatabase localDatabase) {
        return localDatabase.getGroceryIngredientDao();
    }

    @Singleton
    @Provides
    RecipeDao providesRecipeIngredientDao(LocalDatabase localDatabase) {
        return localDatabase.getRecipeIngredientDao();
    }

    @Singleton
    @Provides
    IngredientDao provideIngredientDao(LocalDatabase localDatabase) {
        return localDatabase.getIngredientDao();
    }

    @Singleton
    @Provides
    DatabaseAccess provideDatabaseAccess(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao) {
        return new DatabaseAccessImpl(groceryDao, recipeDao, ingredientDao);
    }

}
