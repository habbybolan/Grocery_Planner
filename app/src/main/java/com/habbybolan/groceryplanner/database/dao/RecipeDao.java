package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.LikedRecipeEntity;
import com.habbybolan.groceryplanner.database.entities.MyRecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeGroceriesTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsWithGroceryCheckTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeNutritionTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeTagTuple;
import com.habbybolan.groceryplanner.models.primarymodels.AccessLevel;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql commands dealing the Recipe and RecipeIngredientBridge queries.
 */
@Dao
public abstract class RecipeDao {

    /*
    RETRIEVING
     */

    @Query("SELECT * FROM RecipeEntity WHERE is_deleted = 0")
    public abstract  List<RecipeEntity> getAllMyRecipes();

    private final String myRecipeFetch = "SELECT * FROM MyRecipeEntity" +
                                       "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0)" +
                                       "   USING (recipeId) ";

    private final String likedRecipeFetch = "SELECT * FROM LikedRecipeEntity" +
                                        "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0)" +
                                        "   USING (recipeId) ";

    @Query(myRecipeFetch + "ORDER BY name ASC")
    public abstract  List<RecipeEntity> getMyRecipesSortAlphabeticalAsc();

    @Query(likedRecipeFetch + "ORDER BY name ASC")
    public abstract  List<RecipeEntity> getLikedRecipesSortAlphabeticalAsc();

    @Query(myRecipeFetch + "ORDER BY name DESC")
    public abstract  List<RecipeEntity> getMyRecipesSortAlphabeticalDesc();

    @Query(likedRecipeFetch + "ORDER BY name DESC")
    public abstract  List<RecipeEntity> getLikedRecipesSortAlphabeticalDesc();

    @Query(myRecipeFetch + "ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getMyRecipesSortDateDesc();

    @Query(likedRecipeFetch + "ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getLikedRecipesSortDateDesc();

    @Query(myRecipeFetch + "ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getMyRecipesSortDateAsc();

    @Query(likedRecipeFetch + "ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getLikedRecipesSortDateAsc();

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY name ASC")
    public abstract List<RecipeEntity> getMyRecipesSortAlphabeticalAsc(long categoryId);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY name ASC")
    public abstract List<RecipeEntity> getLikedRecipesSortAlphabeticalAsc(long categoryId);

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY name DESC")
    public abstract List<RecipeEntity> getMyRecipesSortAlphabeticalDesc(long categoryId);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY name DESC")
    public abstract List<RecipeEntity> getLikedRecipesSortAlphabeticalDesc(long categoryId);

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getMyRecipesSortDateDesc(long categoryId);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY date_created DESC")
    public abstract  List<RecipeEntity> getLikedRecipesSortDateDesc(long categoryId);

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getMyRecipesSortDateAsc(long categoryId);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity " +
            "       WHERE is_deleted = 0 " +
            "       AND recipe_category_id = :categoryId)" +
            "   USING (recipeId)" +
            "ORDER BY date_created ASC")
    public abstract  List<RecipeEntity> getLikedRecipesSortDateAsc(long categoryId);

    @Query("SELECT * FROM RecipeCategoryEntity")
    public abstract  List<RecipeCategoryEntity> getAllRecipeCategories();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name ASC")
    public abstract List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalAsc();

    @Query("SELECT * FROM RecipeCategoryEntity ORDER BY name DESC")
    public abstract List<RecipeCategoryEntity> getAllRecipeCategoriesSortAlphabeticalDesc();

    @Query("SELECT * FROM RecipeCategoryEntity WHERE recipeCategoryId = :recipeCategoryId")
    public abstract RecipeCategoryEntity getRecipeCategory(long recipeCategoryId);

    @Query("SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND recipe_category_id = NULL")
    public abstract List<RecipeEntity> getAllUnCategorizedRecipes();

    @Query("SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND recipeId = :recipeId")
    abstract RecipeEntity getRecipe(long recipeId);

    @Query("SELECT * FROM MyRecipeEntity WHERE recipeId = :recipeId")
    abstract MyRecipeEntity getMyRecipe(long recipeId);

    @Transaction
    public MyRecipe getFullMyRecipe(long recipeId) {
        RecipeEntity recipeEntity = getRecipe(recipeId);
        MyRecipeEntity myRecipeEntity = getMyRecipe(recipeId);
        List<RecipeIngredientsTuple> ingredientsTuple = getRecipeIngredients(recipeId);
        List<RecipeTagTuple> recipeTagTuples = getRecipeTags(recipeId);
        List<RecipeNutritionTuple> nutritionTuples = getRecipeNutrition(recipeId);
        List<Nutrition> nutritionList = new ArrayList<>();
        for (RecipeNutritionTuple tuple : nutritionTuples)
            nutritionList.add(new Nutrition(tuple.nutritionId, tuple.amount, tuple.unitOfMeasurementId, tuple.dateUpdated, tuple.dateSynchronized));
        OfflineRecipe offlineRecipe = new OfflineRecipe(recipeEntity.getName(), recipeEntity.getRecipeId(), recipeEntity.getOnlineRecipeId(), recipeEntity.getIsFavorite(), recipeEntity.getDescription(),
                recipeEntity.getPrepTime(), recipeEntity.getCookTime(), recipeEntity.getServingSize(), recipeEntity.getRecipeCategoryId(), recipeEntity.getInstructions(), recipeEntity.getDateCreated(),
                recipeEntity.getDateUpdated(), recipeEntity.getDateSynchronized(), OfflineRecipe.convertToRecipeTag(recipeTagTuples), OfflineRecipe.convertTuplesToIngredients(ingredientsTuple), nutritionList);
        return new MyRecipe(offlineRecipe, new AccessLevel((int) myRecipeEntity.accessLevelId));
    }

    @Query("SELECT * FROM LikedRecipeEntity WHERE recipeId = :recipeId")
    abstract LikedRecipeEntity getLikedRecipe(long recipeId);

    @Transaction
    public LikedRecipe getFullLikedRecipe(long recipeId) {
        RecipeEntity recipeEntity = getRecipe(recipeId);
        // TODO: use more liked recipe values when added
        LikedRecipeEntity likedRecipeEntity = getLikedRecipe(recipeId);
        List<RecipeIngredientsTuple> ingredientsTuple = getRecipeIngredients(recipeId);
        List<RecipeTagTuple> recipeTagTuples = getRecipeTags(recipeId);
        List<RecipeNutritionTuple> nutritionTuples = getRecipeNutrition(recipeId);
        List<Nutrition> nutritionList = new ArrayList<>();
        for (RecipeNutritionTuple tuple : nutritionTuples)
            nutritionList.add(new Nutrition(tuple.nutritionId, tuple.amount, tuple.unitOfMeasurementId, tuple.dateUpdated, tuple.dateSynchronized));
        OfflineRecipe offlineRecipe = new OfflineRecipe(recipeEntity.getName(), recipeEntity.getRecipeId(), recipeEntity.getOnlineRecipeId(), recipeEntity.getIsFavorite(), recipeEntity.getDescription(),
                recipeEntity.getPrepTime(), recipeEntity.getCookTime(), recipeEntity.getServingSize(), recipeEntity.getRecipeCategoryId(), recipeEntity.getInstructions(), recipeEntity.getDateCreated(),
                recipeEntity.getDateUpdated(), recipeEntity.getDateSynchronized(), OfflineRecipe.convertToRecipeTag(recipeTagTuples), OfflineRecipe.convertTuplesToIngredients(ingredientsTuple), nutritionList);
        return new LikedRecipe(offlineRecipe);
    }

    @Query("SELECT nutrition_id, amount, unit_of_measurement_id, date_updated, date_synchronized " +
            "FROM NutritionEntity " +
            "INNER JOIN (SELECT nutrition_id, recipe_id, amount, unit_of_measurement_id, date_synchronized, date_updated FROM RecipeNutritionBridge WHERE is_deleted = 0) " +
            "USING (nutrition_id) WHERE recipe_id = :recipeId")
    public abstract List<RecipeNutritionTuple> getRecipeNutrition(long recipeId);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineingredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE is_deleted = 0 AND recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId)" +
            "       JOIN" +
            "       (SELECT recipeId, onlineRecipeId FROM recipeentity WHERE recipeId = :recipeId)" +
            "       USING (recipeId)")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredients(long recipeId);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE is_deleted = 0 AND recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) " +
            "       JOIN " +
            "       (SELECT recipeId, onlineRecipeId FROM recipeEntity WHERE recipeId = :recipeId)" +
            "       USING (recipeId)" +
            "       ORDER BY ingredientName ASC")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalAsc(long recipeId);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, date_updated, date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE is_deleted = 0 AND recipeId = :recipeId) " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName,  food_type_id " +
            "           FROM IngredientEntity) " +
            "       USING (ingredientId) " +
            "       JOIN " +
            "       (SELECT recipeId, onlineRecipeId FROM RecipeEntity WHERE recipeId = :recipeId)" +
            "       USING (recipeId)" +
            "       ORDER BY ingredientName DESC")
    public abstract List<RecipeIngredientsTuple> getRecipeIngredientsSortAlphabeticalDesc(long recipeId);

    @Query("SELECT groceryId, ingredientId, ingredientName, " +
            "quantity, quantityMeasId, date_updated, date_synchronized food_type_id" +
            "   FROM " +
            "   (SELECT ingredientId, ingredientName, " +
            "       quantity, quantityMeasId, date_updated, date_synchronized, food_type_id " +
            "       FROM " +
            "           (SELECT ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "               FROM RecipeIngredientBridge WHERE is_deleted = 0 AND recipeId = :recipeId) " +
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

    @Query("SELECT * FROM IngredientEntity " +
            "WHERE ingredientId NOT IN " +
            "(SELECT RIB.ingredientId FROM RecipeIngredientBridge RIB " +
            "WHERE is_deleted = 0 AND RIB.recipeId == :recipeId)")
    public abstract List<IngredientEntity> getIngredientsNotInRecipe(long recipeId);

    @Query("SELECT * FROM GroceryEntity " +
            "WHERE groceryId NOT IN " +
            "(SELECT GRB.groceryId FROM GroceryRecipeBridge GRB " +
            "WHERE GRB.recipeId == :recipeId)")
    public abstract List<GroceryEntity> getGroceriesNotHoldingRecipe(long recipeId);

    @Query("SELECT groceryId, onlineGroceryId, groceryName, amount " +
            "   FROM" +
            "   (SELECT groceryId, amount FROM GroceryRecipeBridge WHERE recipeId=:recipeId) " +
            "   JOIN" +
            "   (SELECT groceryId, onlineGroceryId, name AS groceryName FROM GroceryEntity) " +
            "   USING (groceryId)")
    public abstract List<RecipeGroceriesTuple> getGroceriesHoldingRecipe(long recipeId);

    @Query("SELECT tagId, onlineTagId, title, date_updated, date_synchronized FROM " +
            "   (SELECT tagId, date_updated, date_synchronized FROM RecipeTagBridge WHERE is_deleted = 0 AND recipeId=:recipeId)" +
            "   JOIN" +
            "   (SELECT tagId, onlineTagId, title FROM RecipeTagEntity)" +
            "   USING  (tagId)")
    public abstract List<RecipeTagTuple> getRecipeTags(long recipeId);

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND name like :recipeSearch)" +
            "USING (recipeId)")
    public abstract List<RecipeEntity> searchMyRecipes(String recipeSearch);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND name like :recipeSearch)" +
            "USING (recipeId)")
    public abstract List<RecipeEntity> searchLikedRecipes(String recipeSearch);

    @Query("SELECT * FROM MyRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND name like :recipeSearch AND recipe_category_id = :categoryId)" +
            "USING (recipeId)")
    public abstract List<RecipeEntity> searchMyRecipesInCategory(long categoryId, String recipeSearch);

    @Query("SELECT * FROM LikedRecipeEntity" +
            "   INNER JOIN (SELECT * FROM RecipeEntity WHERE is_deleted = 0 AND name like :recipeSearch AND recipe_category_id = :categoryId)" +
            "USING (recipeId)")
    public abstract List<RecipeEntity> searchLikedRecipesInCategory(long categoryId, String recipeSearch);

    @Query("SELECT * FROM RecipeCategoryEntity WHERE name like :categorySearch")
    public abstract List<RecipeCategoryEntity> searchRecipeCategories(String categorySearch);

    @Query("SELECT recipeId, onlineRecipeId, ingredientId, onlineIngredientId, ingredientName, " +
            "   quantity, quantityMeasId, food_type_id, RIB.date_updated, RIB.date_synchronized " +
            "   FROM " +
            "       (SELECT recipeId, ingredientId, quantity, quantity_meas_id AS quantityMeasId, date_updated, date_synchronized " +
            "           FROM RecipeIngredientBridge WHERE is_deleted = 0 AND recipeId = :recipeId) AS RIB " +
            "       JOIN " +
            "       (SELECT ingredientId, onlineIngredientId, ingredientName, food_type_id " +
            "           FROM IngredientEntity WHERE ingredientName LIKE :ingredientSearch) " +
            "       USING (ingredientId)" +
            "       JOIN (SELECT recipeId, onlineRecipeId FROM RecipeEntity WHERE is_deleted = 0) " +
            "       USING (recipeId)")
    public abstract List<RecipeIngredientsTuple> searchRecipeIngredients(long recipeId, String ingredientSearch);

    /*
    INSERTING/UPDATING
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertRecipe(RecipeEntity recipeEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertMyRecipe(MyRecipeEntity myRecipeEntity);

    @Transaction
    public long insertNewMyRecipe(RecipeEntity recipeEntity) {
        long id = insertRecipe(recipeEntity);
        MyRecipeEntity myRecipeEntity = new MyRecipeEntity(id, AccessLevel.ADMIN_ID);
        updateRecipeDateUpdated(id);
        insertMyRecipe(myRecipeEntity);
        return id;
    }

    @Transaction
    public void updateRecipe(RecipeEntity recipeEntity) {
        updateRecipeData(recipeEntity);
        updateRecipeDateUpdated(recipeEntity.getRecipeId());
    }

    @Update
    abstract void updateRecipeData(RecipeEntity recipeEntity);

    @Query("UPDATE RecipeEntity SET date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') WHERE recipeId = :recipeId")
    abstract void updateRecipeDateUpdated(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipeCategory(RecipeCategoryEntity recipeCategoryEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipeIntoGrocery(GroceryRecipeBridge groceryRecipeBridge);


    @Query("SELECT ingredientName FROM IngredientEntity WHERE ingredientName = :name LIMIT 1")
    public abstract long hasIngredientName(String name);

    /*
    DELETING
     */

    /**
     * Set the delete flag for the list of recipes and all of the bridge tables they're associated with.
     * @param recipeIds Ids of the recipes to set the delete flag for
     */
    @Transaction
    public void deleteRecipes(List<Long> recipeIds) {
        deleteFlagRecipes(recipeIds);
        deleteFlagRecipeIngredientBridges(recipeIds);
        deleteFlagRecipeTagBridges(recipeIds);
        deleteFlagRecipeNutritionBridges(recipeIds);
    }

    @Query("UPDATE RecipeEntity SET is_deleted = 1, date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')" +
            "   WHERE recipeId IN (:recipeIds)")
    public abstract void deleteFlagRecipes(List<Long> recipeIds);

    @Query("UPDATE RecipeIngredientBridge SET is_deleted = 1, date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') " +
            "   WHERE recipeId IN (:recipeIds)")
    public abstract void deleteFlagRecipeIngredientBridges(List<Long> recipeIds);

    @Query("UPDATE RecipeTagBridge SET is_deleted = 1, date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') " +
            "   WHERE recipeId IN (:recipeIds)")
    public abstract void deleteFlagRecipeTagBridges(List<Long> recipeIds);

    @Query("UPDATE RecipeNutritionBridge SET is_deleted = 1, date_updated = STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW') " +
            "   WHERE recipe_id IN (:recipeIds)")
    public abstract void deleteFlagRecipeNutritionBridges(List<Long> recipeIds);

    @Query("UPDATE RecipeIngredientBridge SET is_deleted = 1 WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    public abstract void deleteFlagIngredientsFromRecipeIngredientBridge(long recipeId, List<Long> ingredientIds);

    @Query("UPDATE RecipeNutritionBridge SET is_deleted = 1 WHERE recipe_id=:recipeId AND nutrition_id = :nutritionId")
    public abstract void deleteFlagNutritionBridge(long recipeId, long nutritionId);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND ingredientId IN (:ingredientIds)")
    public abstract void deleteRecipeIngredientsFromGroceryRecipeIngredientEntity(long recipeId, List<Long> ingredientIds);

    @Query("DELETE FROM GroceryRecipeIngredientEntity WHERE recipeId=:recipeId AND groceryId=:groceryId")
    public abstract void deleteRecipeGroceryFromGroceryRecipeIngredient(long groceryId, long recipeId);

    @Query("DELETE FROM RecipeCategoryEntity WHERE recipeCategoryId IN (:categoryIds)")
    public abstract void deleteRecipeCategories(List<Long> categoryIds);

    @Transaction
    public long syncMyRecipeInsert(long onlineId, String name, String description, String instructions, int prepTime, int cookTime, int servingSize,
                                   Timestamp dateSync, long accessLevel) {
        syncMyRecipeInsertHelper(onlineId, name, description, instructions, prepTime, cookTime, servingSize, dateSync);
        long recipeId = getLastInsertedRecipeRow();
        insertMyRecipe(new MyRecipeEntity(recipeId, accessLevel));
        return recipeId;
    }

    @Query("SELECT last_insert_rowid();")
    abstract long getLastInsertedRecipeRow();

    @Query("INSERT INTO RecipeEntity (onlineRecipeId, name, description, instructions, cook_time, prep_time, serving_size, date_synchronized)" +
            "   VALUES (:onlineId, :name, :description, :instructions, :cookTime, :prepTime, :servingSize, :dateSync)")
    abstract void syncMyRecipeInsertHelper(long onlineId, String name, String description, String instructions, int prepTime, int cookTime, int servingSize, Timestamp dateSync);

    @Transaction
    public void syncMyRecipeUpdate(long recipeId, String name, String description, int prepTime, int cookTime, int servingSize,
                                   String instructions, Timestamp dateSync, long accessLevel) {
        syncRecipeUpdate(recipeId, name, description, prepTime, cookTime, servingSize, instructions, dateSync);
        syncMyRecipeUpdateAccessLevel(recipeId, accessLevel);
    }

    @Transaction
    public long syncLikedRecipeInsert(RecipeEntity recipeEntity) {
        // TODO: inserting liked recipe
        return 0;
    }

    @Transaction
    public void syncLikedRecipeUpdate(long recipeId, String name, String description, int prepTime, int cookTime, int servingSize,
                                      String instructions, Timestamp dateSync) {
        syncRecipeUpdate(recipeId, name, description, prepTime, cookTime, servingSize, instructions, dateSync);
        // TODO: anything else for liked recipe?
    }

    @Query("UPDATE MyRecipeEntity SET accessLevelId = :accessLevel WHERE recipeId = :recipeId")
    abstract void syncMyRecipeUpdateAccessLevel(long recipeId, long accessLevel);

    @Query("UPDATE RecipeEntity SET name = :name, description = :description, prep_time = :prepTime, cook_time = :cookTime," +
            "   serving_size = :servingSize, instructions = :instructions, date_synchronized = :dateSync WHERE recipeId = :recipeId")
    abstract void syncRecipeUpdate(long recipeId, String name, String description, int prepTime, int cookTime, int servingSize,
                                          String instructions, Timestamp dateSync);

    @Query("UPDATE RecipeEntity SET date_synchronized = :dateSync, onlineRecipeId = :onlineRecipeId WHERE recipeId = :recipeId")
    public abstract void syncRecipeInsertedOnline(long recipeId, long onlineRecipeId, Timestamp dateSync);

    @Query("UPDATE RecipeEntity SET date_synchronized = :dateSync WHERE recipeId = :recipeId")
    public abstract void syncRecipeUpdateDateSync(long recipeId, Timestamp dateSync);
}
