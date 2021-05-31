package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeNutritionBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeGroceriesTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsWithGroceryCheckTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeNutritionTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeTagTuple;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.util.ArrayList;
import java.util.List;

/**
 * Sql commands dealing the Recipe and RecipeIngredientBridge queries.
 */
@Dao
public abstract class RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertRecipe(RecipeEntity recipeEntity);

    @Update
    public abstract void updateRecipe(RecipeEntity recipeEntity);

    @Query("SELECT * FROM RecipeEntity")
    public abstract  List<RecipeEntity> getAllRecipes();

    @Query("SELECT * FROM RecipeEntity ORDER BY name ASC")
    public abstract  List<RecipeEntity> getAllRecipesSortAlphabeticalAsc();

    @Query("SELECT * FROM RecipeEntity ORDER BY name DESC")
    public abstract  List<RecipeEntity> getAllRecipesSortAlphabeticalDesc();

    @Query("SELECT * FROM RecipeEntity ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getAllRecipesSortDateDesc();

    @Query("SELECT * FROM RecipeEntity ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getAllRecipesSortDateAsc();

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId")
    public abstract List<RecipeEntity> getAllRecipes(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY name ASC")
    public abstract List<RecipeEntity> getAllRecipesSortAlphabeticalAsc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY name DESC")
    public abstract List<RecipeEntity> getAllRecipesSortAlphabeticalDesc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getAllRecipesSortDateDesc(long categoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :categoryId ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getAllRecipesSortDateAsc(long categoryId);

    @Query("SELECT * FROM RecipeCategoryEntity")
    public abstract  List<RecipeCategoryEntity> getAllRecipeCategories();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name ASC")
    public abstract List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalAsc();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name DESC")
    public abstract List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalDesc();

    @Query("SELECT * FROM RecipeCategoryEntity WHERE recipeCategoryId = :recipeCategoryId")
    public abstract RecipeCategoryEntity getRecipeCategory(long recipeCategoryId);

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = NULL")
    public abstract List<RecipeEntity> getAllUnCategorizedRecipes();



    @Delete
    public abstract void deleteRecipe(RecipeEntity recipeEntity);

    @Query("DELETE FROM RecipeEntity WHERE recipeId IN (:recipeIds)")
    public abstract void deleteRecipes(List<Long> recipeIds);

    @Query("SELECT * FROM RecipeEntity WHERE recipeId = :recipeId")
    public abstract RecipeEntity getRecipe(long recipeId);

    @Transaction
    public OfflineRecipe getFullRecipe(long recipeId) {
        RecipeEntity recipeEntity = getRecipe(recipeId);
        List<RecipeIngredientsTuple> ingredientsTuple = getRecipeIngredients(recipeId);
        List<RecipeTagTuple> recipeTagTuples = getRecipeTags(recipeId);
        List<RecipeNutritionTuple> nutritionTuples = getRecipeNutrition(recipeId);
        List<Nutrition> nutritionList = new ArrayList<>();
        for (RecipeNutritionTuple tuple : nutritionTuples)
            nutritionList.add(new Nutrition(tuple.nutritionId, tuple.amount, tuple.unitOfMeasurementId, tuple.dateUpdated, tuple.dateSynchronized));
        return new OfflineRecipe(recipeEntity.getName(), recipeEntity.getRecipeId(), recipeEntity.getOnlineRecipeId(), recipeEntity.getIsFavorite(), recipeEntity.getDescription(),
                recipeEntity.getPrepTime(), recipeEntity.getCookTime(), recipeEntity.getServingSize(), recipeEntity.getRecipeCategoryId(), recipeEntity.getInstructions(), recipeEntity.getDateCreated(),
                recipeEntity.getDateSynchronized(), OfflineRecipe.convertToRecipeTag(recipeTagTuples), OfflineRecipe.convertTuplesToIngredients(ingredientsTuple), nutritionList);
    }

    @Query("SELECT nutrition_id, amount, unit_of_measurement_id, date_updated, date_synchronized " +
            "FROM NutritionEntity " +
            "INNER JOIN RecipeNutritionBridge " +
            "USING (nutrition_id) WHERE recipe_id = :recipeId")
    public abstract List<RecipeNutritionTuple> getRecipeNutrition(long recipeId);

    // Bridge table with Ingredients

    @Delete
    public abstract void deleteFromRecipeIngredientBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    public abstract void deleteIngredientsFromRecipeIngredientBridge(long recipeId, List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    public abstract void deleteRecipeIngredientsFromGroceryRecipeIngredientEntity(long recipeId, List<Long> ingredientIds);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineingredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId)" +
            "       JOIN" +
            "       (SELECT recipeId, onlineRecipeId FROM recipeentity)" +
            "       USING (recipeId)")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredients(long recipeId);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) " +
            "       JOIN " +
            "       (SELECT recipeId, onlineRecipeId FROM recipeEntity)" +
            "       USING (recipeId)" +
            "       ORDER BY ingredientName ASC")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalAsc(long recipeId);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName,  food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) " +
            "       JOIN " +
            "       (SELECT recipeId, onlineRecipeId FROM RecipeEntity)" +
            "       USING (recipeId)" +
            "       ORDER BY ingredientName DESC")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalDesc(long recipeId);

    @Query("SELECT groceryId, ingredientId, ingredientName, " +
            "quantity, quantityMeasId, food_type_id" +
            "   FROM " +
            "   (SELECT ingredientId, ingredientName, " +
            "       quantity, quantityMeasId, food_type_id " +
            "       FROM " +
            "           (SELECT ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "               FROM RecipeIngredientBridge WHERE recipeId = :recipeId) " +
            "           JOIN " +
            "           (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "               FROM IngredientEntity) " +
            "           USING (ingredientId))" +
            "   LEFT JOIN" +
            "       (SElECT groceryId, ingredientId " +
            "           FROM GroceryRecipeIngredientEntity" +
            "           WHERE recipeId = :recipeId AND groceryId = :groceryId) " +
            "   USING (ingredientId)")
    public abstract List<RecipeIngredientsWithGroceryCheckTuple> getRecipeIngredientsInGrocery(long recipeId, long groceryId);

    // Recipe Category

    @Query("SELECT * FROM RecipeEntity WHERE recipe_category_id = :id")
    public abstract List<RecipeEntity> getRecipesInCategory(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId IN (:recipeIds)")
    public abstract void deleteRecipesFromRecipeIngredientBridge(List<Long> recipeIds);

    @Query("DELETE FROM RecipeCategoryEntity WHERE recipeCategoryId IN (:categoryIds)")
    public abstract void deleteRecipeCategories(List<Long> categoryIds);



    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT RIB.ingredientId FROM RecipeIngredientBridge RIB " +
            "WHERE RIB.recipeId == :recipeId)")
    public abstract List<IngredientEntity> getIngredientsNotInRecipe(long recipeId);

    @Delete
    public abstract void deleteRecipeFromGroceryInGroceryRecipeBridge(GroceryRecipeBridge groceryRecipeBridge);

    @Query("DELETE FROM RecipeIngredientBridge WHERE recipeId IN (:recipeIds)")
    public abstract void deleteRecipesFromGroceryRecipeBridge(List<Long> recipeIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId IN (:recipeIds)")
    public abstract void deleteRecipeFromGroceryRecipeIngredient(List<Long> recipeIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND groceryId=:groceryId")
    public abstract void deleteRecipeGroceryFromGroceryRecipeIngredient(long groceryId, long recipeId);

    @Query("SELECT * FROM GroceryEntity " +
            "WHERE groceryId NOT IN " +
            "(SELECT GRB.groceryId FROM GroceryRecipeBridge GRB " +
            "WHERE GRB.recipeId == :recipeId)")
    public abstract List<GroceryEntity> getGroceriesNotHoldingRecipe(long recipeId);

    @Query("SELECT * FROM GroceryRecipeBridge WHERE recipeId = :recipeId")
    public abstract List<GroceryRecipeBridge> getRecipeAmountsInsideGrocery(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipeIntoGrocery(GroceryRecipeBridge groceryRecipeBridge);

    @Query("SELECT groceryId, onlineGroceryId, groceryName, amount " +
            "   FROM" +
            "   (SELECT groceryId, amount FROM GroceryRecipeBridge WHERE recipeId=:recipeId) " +
            "   JOIN" +
            "   (SELECT groceryId, onlineGroceryId, name AS groceryName FROM GroceryEntity) " +
            "   USING (groceryId)")
    public abstract List<RecipeGroceriesTuple> getGroceriesHoldingRecipe(long recipeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertTag(RecipeTagEntity tag);

    @Update
    public abstract void updateTag(RecipeTagEntity tag);

    @Query("SELECT tagId FROM RecipeTagEntity WHERE title = :name LIMIT 1")
    public abstract long hasTagName(String name);

    @Transaction
    public void insertUpdateRecipeTagsIntoBridge(List<RecipeTagEntity> tags, long recipeId) {
        for (RecipeTagEntity tag : tags) {
            long tagId = hasTagName(tag.title);
            // tag exists, so update and set is_updated as false
            if (tagId != 0) {
                tag.tagId = tagId;
                updateTag(tag);
                // otherwise tag doesn't exist, so insert
            } else {
                tagId = insertTag(tag);
            }
            // associate the tag with the recipe
            insertRecipeTagBridge(new RecipeTagBridge(recipeId, tagId));
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertRecipeTagBridge(RecipeTagBridge bridge);

    /**
     * Insert or update the Ingredients into the IngredientEntity table, depending on if it exists yet or not.
     * Insert the relationship between the Ingredient and recipeId inside the RecipeIngredientBridge Table.
     * @param ingredients       List of ingredients to insert/update and create the relationship between recipeId and ingredient.
     * @param recipeId          The id of the recipe to insert the ingredients into
     * @param ingredientDao     Holds the Dao method for inserting the ingredients into IngredientEntity Table
     */
    @Transaction
    public void insertUpdateIngredientsIntoRecipe(List<Ingredient> ingredients, long recipeId, IngredientDao ingredientDao) {
        for (Ingredient ingredient : ingredients) {
            IngredientEntity entity = new IngredientEntity(ingredient);
            // if id = 0, then look if an ingredient of that name exists, otherwise create a new Ingredient
            if (entity.getIngredientId() == 0) {
                long id = hasIngredientName(entity.getIngredientName());
                // if 0, then ingredient doesn't exist. Insert it and retrieve id.
                if (id == 0) {
                    id = ingredientDao.insertIngredient(entity);
                }
                entity.setIngredientId(id);
            }
            insertIngredientBridge(new RecipeIngredientBridge(recipeId, entity.getIngredientId(), ingredient.getQuantity(), ingredient.getQuantityMeasId()));
            updateIngredientBridge(recipeId, entity.getIngredientId(), ingredient.getQuantity(), ingredient.getQuantityMeasId());
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertIngredientBridge(RecipeIngredientBridge recipeIngredientBridge);

    @Query("Update RecipeIngredientBridge SET" +
            "   quantity = :quantity," +
            "   quantity_meas_id = :quantityMeasId" +
            "   WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void updateIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId);



    @Query("UPDATE RecipeIngredientBridge" +
            "   SET quantity = :quantity," +
            "       quantity_meas_id = :quantityMeasId" +
            "   WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    public abstract void updateRecipeIngredientBridge(long recipeId, long ingredientId, float quantity, Long quantityMeasId);

    @Query("SELECT ingredientName FROM IngredientEntity WHERE ingredientName = :name LIMIT 1")
    public abstract long hasIngredientName(String name);



    @Query("SELECT tagId, onlineTagId, title, date_updated, date_synchronized FROM " +
            "   (SELECT tagId, date_updated, date_synchronized FROM RecipeTagBridge WHERE recipeId=:recipeId)" +
            "   JOIN" +
            "   (SELECT tagId, onlineTagId, title FROM RecipeTagEntity)" +
            "   USING  (tagId)")
    public abstract List<RecipeTagTuple> getRecipeTags(long recipeId);

    @Delete
    public abstract void deleteRecipeTagBridge(RecipeTagBridge bridge);

    @Query("DELETE FROM RecipeTagBridge WHERE recipeId = :recipeId AND" +
            "   tagId IN (SELECT tagId FROM RecipeTagEntity WHERE title = :title)")
    public abstract void deleteRecipeTagBridgeByTitle(long recipeId, String title);

    @Query("SELECT tagId, onlineTagId, title FROM RecipeTagEntity WHERE title=:title")
    public abstract RecipeTagEntity getRecipeTag(String title);

    @Query("SELECT * FROM RecipeEntity WHERE name like :recipeSearch")
    public abstract List<RecipeEntity> searchRecipes(String recipeSearch);

    @Query("SELECT * FROM RecipeCategoryEntity WHERE name like :categorySearch")
    public abstract List<RecipeCategoryEntity> searchRecipeCategories(String categorySearch);

    @Query("SELECT * FROM RecipeEntity WHERE name like :recipeSearch AND recipe_category_id = :categoryId")
    public abstract List<RecipeEntity> searchRecipesInCategory(long categoryId, String recipeSearch);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, RIB.date_updated, RIB.date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE recipeId = :recipeId) AS RIB " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch) " +
            "       USING (ingredientId)" +
            "       JOIN RecipeEntity USING (recipeId)")
    public abstract List<RecipeIngredientsTuple> searchRecipeIngredients(long recipeId, String ingredientSearch);

    @Delete
    public abstract void deleteNutrition(RecipeNutritionBridge recipeNutritionBridge);

    @Transaction
    public void insertUpdateNutritionBridge(RecipeNutritionBridge bridge) {
        insertNutritionBridge(bridge);
        updateNutritionBridge(bridge.recipeId, bridge.nutritionId, bridge.amount, bridge.unitOfMeasurementId);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertNutritionBridge(RecipeNutritionBridge bridge);

    @Query("UPDATE RecipeNutritionBridge SET" +
            "   amount = :amount," +
            "   unit_of_measurement_id = :unitOfMeasurementId" +
            "   WHERE recipe_id = :recipeId AND nutrition_id = :nutritionId")
    public abstract void updateNutritionBridge(long recipeId, long nutritionId, int amount, Long unitOfMeasurementId);
}
