package com.habbybolan.groceryplanner.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.entities.FoodTypeEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.NutritionEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeNutritionBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.database.entities.UnitOfMeasurementEntity;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.sql.Timestamp;

@androidx.room.Database(entities = {GroceryEntity.class, IngredientEntity.class, GroceryIngredientBridge.class, RecipeEntity.class, RecipeIngredientBridge.class,
        RecipeCategoryEntity.class, GroceryRecipeBridge.class, GroceryRecipeIngredientEntity.class, GroceryIngredientEntity.class, RecipeTagEntity.class,
        RecipeTagBridge.class, UnitOfMeasurementEntity.class, RecipeNutritionBridge.class, NutritionEntity.class, FoodTypeEntity.class},
        exportSchema = false, version = 1)
@TypeConverters({LocalDatabase.Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    public abstract GroceryDao getGroceryIngredientDao();
    public abstract RecipeDao getRecipeIngredientDao();
    public abstract IngredientDao getIngredientDao();

    public static class Converters {
        @TypeConverter
        public static Timestamp fromTimestamp(String value) {
            return value == null ? null : Timestamp.valueOf(value);
        }

        @TypeConverter
        public static String dateToTimestamp(Timestamp date) {
            return date == null ? null : date.toString();
        }
    }

    /**
     * Create a callback for pre-populating the database with the unchanging UnitOfMeasurementEntity rows.
     * @return  The callback to pre-populate the database
     */
    public static RoomDatabase.Callback createCallback() {
        final String UOMTable = "UnitOfMeasurementEntity";
        final String nameIdCol = "id";
        final String nameTypeCol = "type";
        final String nameTypeCodeCol = "type_code";

        final String nutritionTable = "NutritionEntity";
        final String nutritionId = "nutrition_id";
        final String nutritionName = "nutrition_name";

        final String foodTypeTable = "FoodTypeEntity";
        final String foodTypeId = "food_type_id";
        final String foodTypeName = "name";

        return new RoomDatabase.Callback() {

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db){
                // prevent recursion
                db.execSQL("PRAGMA recursive_triggers = OFF;");
            }

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                // Trigger to update date_updated on RecipeEntity update
                db.execSQL("CREATE TRIGGER recipe_update_trigger AFTER UPDATE " +
                        "       ON RecipeEntity" +
                        "       BEGIN" +
                        "           UPDATE RecipeEntity SET date_updated=STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
                        "           WHERE recipeId = NEW.recipeId; " +
                        "       END; ");
                // Trigger to update date_updated on RecipeTagBridge update
                db.execSQL("CREATE TRIGGER recipe_tag_update_trigger AFTER INSERT " +
                        "       ON RecipeTagBridge" +
                        "       BEGIN" +
                        "           UPDATE RecipeTagBridge SET date_updated=STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
                        "           WHERE recipeId = NEW.recipeId AND tagId = New.tagId; " +
                        "       END; ");
                // Trigger to update date_updated on RecipeNutritionBridge update
                db.execSQL("CREATE TRIGGER nutrition_update_trigger AFTER UPDATE " +
                        "       ON RecipeNutritionBridge" +
                        "       BEGIN" +
                        "           UPDATE RecipeNutritionBridge SET date_updated=STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
                        "           WHERE recipe_id = NEW.recipe_id AND nutrition_id = New.nutrition_id; " +
                        "       END; ");
                // Trigger to update date_updated on RecipeIngredientBridge update
                db.execSQL("CREATE TRIGGER recipe_ingredient_update_trigger AFTER UPDATE " +
                        "       ON RecipeIngredientBridge" +
                        "       BEGIN" +
                        "           UPDATE RecipeIngredientBridge SET date_updated=STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
                        "           WHERE recipeId = NEW.recipeId AND ingredientId = New.ingredientId; " +
                        "       END; ");

                // set measurement values

                // pound
                ContentValues cvPound = new ContentValues(3);
                cvPound.put(nameIdCol, MeasurementType.POUND_ID);
                cvPound.put(nameTypeCol, MeasurementType.POUND);
                cvPound.put(nameTypeCodeCol, MeasurementType.POUND_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvPound);
                // ounce
                ContentValues cvOunce = new ContentValues(3);
                cvOunce.put(nameIdCol, MeasurementType.OUNCE_ID);
                cvOunce.put(nameTypeCol, MeasurementType.OUNCE);
                cvOunce.put(nameTypeCodeCol, MeasurementType.OUNCE_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvOunce);
                // milligram
                ContentValues cvMilligram = new ContentValues(3);
                cvMilligram.put(nameIdCol, MeasurementType.MILLIGRAM_ID);
                cvMilligram.put(nameTypeCol, MeasurementType.MILLIGRAM);
                cvMilligram.put(nameTypeCodeCol, MeasurementType.MILLIGRAM_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvMilligram);
                // gram
                ContentValues cvGram = new ContentValues(3);
                cvGram.put(nameIdCol, MeasurementType.GRAM_ID);
                cvGram.put(nameTypeCol, MeasurementType.GRAM);
                cvGram.put(nameTypeCodeCol, MeasurementType.GRAM_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvGram);
                // kilogram
                ContentValues cvKg = new ContentValues(3);
                cvKg.put(nameIdCol, MeasurementType.KILOGRAM_ID);
                cvKg.put(nameTypeCol, MeasurementType.KILOGRAM);
                cvKg.put(nameTypeCodeCol, MeasurementType.KILOGRAM_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvKg);
                // teaspoon
                ContentValues cvTsp = new ContentValues(3);
                cvTsp.put(nameIdCol, MeasurementType.TEASPOON_ID);
                cvTsp.put(nameTypeCol, MeasurementType.TEASPOON);
                cvTsp.put(nameTypeCodeCol, MeasurementType.TEASPOON_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvTsp);
                // tablespoon
                ContentValues cvTbsp = new ContentValues(3);
                cvTbsp.put(nameIdCol, MeasurementType.TABLESPOON_ID);
                cvTbsp.put(nameTypeCol, MeasurementType.TABLESPOON);
                cvTbsp.put(nameTypeCodeCol, MeasurementType.TABLESPOON_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvTbsp);
                // gill
                ContentValues cvGill = new ContentValues(3);
                cvGill.put(nameIdCol, MeasurementType.GILL_ID);
                cvGill.put(nameTypeCol, MeasurementType.GILL);
                cvGill.put(nameTypeCodeCol, MeasurementType.GILL_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvGill);
                // cup
                ContentValues cvCup = new ContentValues(3);
                cvCup.put(nameIdCol, MeasurementType.CUP_ID);
                cvCup.put(nameTypeCol, MeasurementType.CUP);
                cvCup.put(nameTypeCodeCol, MeasurementType.CUP_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvCup);
                // pint
                ContentValues cvPint = new ContentValues(3);
                cvPint.put(nameIdCol, MeasurementType.PINT_ID);
                cvPint.put(nameTypeCol, MeasurementType.PINT);
                cvPint.put(nameTypeCodeCol, MeasurementType.PINT_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvPint);
                // quart
                ContentValues cvQt = new ContentValues(3);
                cvQt.put(nameIdCol, MeasurementType.QUART_ID);
                cvQt.put(nameTypeCol, MeasurementType.QUART);
                cvQt.put(nameTypeCodeCol, MeasurementType.QUART_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvQt);
                // gallon
                ContentValues cvGal = new ContentValues(3);
                cvGal.put(nameIdCol, MeasurementType.GALLON_ID);
                cvGal.put(nameTypeCol, MeasurementType.GALLON);
                cvGal.put(nameTypeCodeCol, MeasurementType.GALLON_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvGal);
                // milliliter
                ContentValues cvMl = new ContentValues(3);
                cvMl.put(nameIdCol, MeasurementType.MILLILITER_ID);
                cvMl.put(nameTypeCol, MeasurementType.MILLILITER);
                cvMl.put(nameTypeCodeCol, MeasurementType.MILLIGRAM_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvMl);
                // liter
                ContentValues cvLiter = new ContentValues(3);
                cvLiter.put(nameIdCol, MeasurementType.LITER_ID);
                cvLiter.put(nameTypeCol, MeasurementType.LITER);
                cvLiter.put(nameTypeCodeCol, MeasurementType.LITER_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvLiter);
                // deciliter
                ContentValues cvDl = new ContentValues(3);
                cvDl.put(nameIdCol, MeasurementType.DECILITER_ID);
                cvDl.put(nameTypeCol, MeasurementType.DECILITER);
                cvDl.put(nameTypeCodeCol, MeasurementType.DECILITER_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvDl);
                // millimeter
                ContentValues cvMm = new ContentValues(3);
                cvMm.put(nameIdCol, MeasurementType.MILLIMETER_ID);
                cvMm.put(nameTypeCol, MeasurementType.MILLIMETER);
                cvMm.put(nameTypeCodeCol, MeasurementType.MILLIMETER_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvMm);
                // centimeter
                ContentValues cvCm = new ContentValues(3);
                cvCm.put(nameIdCol, MeasurementType.CENTIMETER_ID);
                cvCm.put(nameTypeCol, MeasurementType.CENTIMETER);
                cvCm.put(nameTypeCodeCol, MeasurementType.CENTIMETER_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvCm);
                // meter
                ContentValues cvMeter = new ContentValues(3);
                cvMeter.put(nameIdCol, MeasurementType.METER_ID);
                cvMeter.put(nameTypeCol, MeasurementType.METER);
                cvMeter.put(nameTypeCodeCol, MeasurementType.METER_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvMeter);
                // inch
                ContentValues cvInch = new ContentValues(3);
                cvInch.put(nameIdCol, MeasurementType.INCH_ID);
                cvInch.put(nameTypeCol, MeasurementType.INCH);
                cvInch.put(nameTypeCodeCol, MeasurementType.INCH_CODE);
                db.insert(UOMTable, SQLiteDatabase.CONFLICT_IGNORE, cvInch);

                // Nutrition

                // Calories
                ContentValues cvCalories = new ContentValues(2);
                cvCalories.put(nutritionId, Nutrition.CALORIES_ID);
                cvCalories.put(nutritionName, Nutrition.CALORIES);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvCalories);
                // Fat
                ContentValues cvFat = new ContentValues(2);
                cvFat.put(nutritionId, Nutrition.FAT_ID);
                cvFat.put(nutritionName, Nutrition.FAT);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvFat);
                // Saturated Fat
                ContentValues cvSaturatedFat = new ContentValues(2);
                cvSaturatedFat.put(nutritionId, Nutrition.SATURATED_FAT_ID);
                cvSaturatedFat.put(nutritionName, Nutrition.SATURATED_FAT);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvSaturatedFat);
                // Carbohydrates
                ContentValues cvCarbohydrates = new ContentValues(2);
                cvCarbohydrates.put(nutritionId, Nutrition.CARBOHYDRATES_ID);
                cvCarbohydrates.put(nutritionName, Nutrition.CARBOHYDRATES);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvCarbohydrates);
                // Fiber
                ContentValues cvFiber = new ContentValues(2);
                cvFiber.put(nutritionId, Nutrition.FIBER_ID);
                cvFiber.put(nutritionName, Nutrition.FIBRE);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvFiber);
                // Sugar
                ContentValues cvSugar = new ContentValues(2);
                cvSugar.put(nutritionId, Nutrition.SUGAR_ID);
                cvSugar.put(nutritionName, Nutrition.SUGAR);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvSugar);
                // Protein
                ContentValues cvProtein = new ContentValues(2);
                cvProtein.put(nutritionId, Nutrition.PROTEIN_ID);
                cvProtein.put(nutritionName, Nutrition.PROTEIN);
                db.insert(nutritionTable, SQLiteDatabase.CONFLICT_IGNORE, cvProtein);

                // Food Types

                // Fruits
                ContentValues cvFruits = new ContentValues(2);
                cvFruits.put(foodTypeId, FoodType.FRUITS_ID);
                cvFruits.put(foodTypeName, FoodType.FRUITS);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvFruits);
                // Vegetables
                ContentValues cvVegetables = new ContentValues(2);
                cvVegetables.put(foodTypeId, FoodType.VEGETABLES_ID);
                cvVegetables.put(foodTypeName, FoodType.VEGETABLES);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvVegetables);
                // Dairy
                ContentValues cvDairy = new ContentValues(2);
                cvDairy.put(foodTypeId, FoodType.DAIRY_ID);
                cvDairy.put(foodTypeName, FoodType.DAIRY);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvDairy);
                // Protein
                ContentValues cvFoodProtein = new ContentValues(2);
                cvFoodProtein.put(foodTypeId, FoodType.PROTEIN_ID);
                cvFoodProtein.put(foodTypeName, FoodType.PROTEIN);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvFoodProtein);
                // Grains
                ContentValues cvGrains = new ContentValues(2);
                cvGrains.put(foodTypeId, FoodType.GRAINS_ID);
                cvGrains.put(foodTypeName, FoodType.GRAINS);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvGrains);
                // Oils
                ContentValues cvOils = new ContentValues(2);
                cvOils.put(foodTypeId, FoodType.OILS_ID);
                cvOils.put(foodTypeName, FoodType.OILS);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvOils);
                // other
                ContentValues cvOther = new ContentValues(2);
                cvOther.put(foodTypeId, FoodType.OTHER_ID);
                cvOther.put(foodTypeName, FoodType.OTHER);
                db.insert(foodTypeTable, SQLiteDatabase.CONFLICT_IGNORE, cvOther);
            }
        };
    }

}
