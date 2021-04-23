package com.habbybolan.groceryplanner.database;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;

import java.util.Date;

@androidx.room.Database(entities = {GroceryEntity.class, IngredientEntity.class, GroceryIngredientBridge.class, RecipeEntity.class, RecipeIngredientBridge.class,
        RecipeCategoryEntity.class, GroceryRecipeBridge.class, GroceryRecipeIngredientEntity.class, GroceryIngredientEntity.class, RecipeTagEntity.class, RecipeTagBridge.class},
        exportSchema = false, version = 1)
@TypeConverters({LocalDatabase.Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    public abstract GroceryDao getGroceryIngredientDao();
    public abstract RecipeDao getRecipeIngredientDao();
    public abstract IngredientDao getIngredientDao();

    public static class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

}
