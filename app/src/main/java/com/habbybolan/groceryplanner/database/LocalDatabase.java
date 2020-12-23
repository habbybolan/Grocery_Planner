package com.habbybolan.groceryplanner.database;

import androidx.room.RoomDatabase;

import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.dao.SectionDao;
import com.habbybolan.groceryplanner.database.dao.StepsDao;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.StepsEntity;

@androidx.room.Database(entities = {GroceryEntity.class, IngredientEntity.class, GroceryIngredientBridge.class, RecipeEntity.class, RecipeIngredientBridge.class,
        StepsEntity.class},
        exportSchema = false, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract GroceryDao getGroceryIngredientDao();
    public abstract RecipeDao getRecipeIngredientDao();
    public abstract IngredientDao getIngredientDao();
    public abstract SectionDao getSectionDao();
    public abstract StepsDao getStepsDao();
}
