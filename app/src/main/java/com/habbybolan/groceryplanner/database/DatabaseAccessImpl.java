package com.habbybolan.groceryplanner.database;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.callbacks.DbCallback;
import com.habbybolan.groceryplanner.callbacks.DbCallbackEmptyResponse;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.NutritionDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.dao.RecipeTagDao;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeNutritionBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.combinedmodels.RecipeWithIngredient;
import com.habbybolan.groceryplanner.models.databasetuples.GroceryIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeGroceriesTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientInGroceryTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsWithGroceryCheckTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeTagTuple;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseAccessImpl implements DatabaseAccess {

    private GroceryDao groceryDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private RecipeTagDao recipeTagDao;
    private NutritionDao nutritionDao;

    public DatabaseAccessImpl(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao, RecipeTagDao recipeTagDao, NutritionDao nutritionDao) {
        this.groceryDao = groceryDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.recipeTagDao = recipeTagDao;
        this.nutritionDao = nutritionDao;
    }

    // GROCERIES

    @Override
    public void deleteGrocery(Long groceryId) {
        deleteGroceries(new ArrayList<Long>(){{add(groceryId);}});
    }

    @Override
    public synchronized void deleteGroceries(List<Long> groceryIds) {
        Runnable task = () -> groceryDao.deleteGroceries(groceryIds);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void addGrocery(Grocery grocery) {
        GroceryEntity groceryEntity = new GroceryEntity(grocery);
        Runnable task = () -> groceryDao.insertGrocery(groceryEntity);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void fetchGroceries(DbCallback<Grocery> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> {
            switch (sortType.getSortKey()) {
                case SortType.SORT_ALPHABETICAL_ASC:
                    return groceryDao.getAllGroceriesSortAlphabeticalAsc();
                case SortType.SORT_ALPHABETICAL_DEC:
                    return groceryDao.getAllGroceriesSortAlphabeticalDesc();
                default:
                    return groceryDao.getAllGroceries();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryEntity>> futureTask = executorService.submit(task);
        List<Grocery> groceries = new ArrayList<>();
        List<GroceryEntity> groceryEntities = futureTask.get();
        for (GroceryEntity groceryEntity : groceryEntities) {
            groceries.add(new Grocery(groceryEntity));
        }
        callback.onResponse(groceries);
    }

    @Override
    public synchronized void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryRecipe>> task = () -> {
            List<RecipeGroceriesTuple> groceries = recipeDao.getGroceriesHoldingRecipe(offlineRecipe.getId());
            List<GroceryRecipe> groceryRecipes = new ArrayList<>();
            for (RecipeGroceriesTuple grocery : groceries) {
                groceryRecipes.add(new GroceryRecipe( grocery.groceryName, grocery.groceryId, grocery.onlineGroceryId, grocery.amount, grocery.dateSynchronized));
            }
            return groceryRecipes;
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryRecipe>> futureTask = executorService.submit(task);
        List<GroceryRecipe> groceryRecipes = futureTask.get();
        callback.onResponse(groceryRecipes);
    }

    @Override
    public synchronized void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> recipeDao.getGroceriesNotHoldingRecipe(offlineRecipe.getId());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryEntity>> futureTask = executorService.submit(task);
        List<GroceryEntity> groceryEntities = futureTask.get();
        List<Grocery> groceries = new ArrayList<>();
        for (GroceryEntity groceryEntity : groceryEntities) {
            groceries.add(new Grocery(groceryEntity));
        }
        callback.onResponse(groceries);
    }

    @Override
    public synchronized void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients) {
        Runnable task = () -> {
            // add the recipe to the GroceryRecipeBridge if it doesn't already exist, or update with new amount
            GroceryRecipeBridge groceryRecipeBridge = new GroceryRecipeBridge(grocery.getId(), offlineRecipe.getId(), amount);
            recipeDao.insertRecipeIntoGrocery(groceryRecipeBridge);

            // insert or remove from GroceryRecipeIngredientEntity and GroceryIngredientBridge
            for (IngredientWithGroceryCheck ingredient : recipeIngredients) {
                GroceryRecipeIngredientEntity entity = new GroceryRecipeIngredientEntity(grocery.getId(),
                        ingredient.getId(), offlineRecipe.getId(), ingredient.getQuantity(), ingredient.getQuantityMeasId());

                // if the ingredient is checked as add to grocery, then add, otherwise delete from grocery
                if (ingredient.getIsInGrocery()) {
                    groceryDao.insertGroceryRecipeIngredient(entity);
                    // todo: could do this much better
                    boolean isGroceryHasIngredient = groceryDao.getCountIngredientInGroceryBridge(grocery.getId(), ingredient.getId()) >= 1;
                    // if the grocery doesn't already contain the ingredient, add it to groceryIngredientBridge
                    if (!isGroceryHasIngredient) {
                        GroceryIngredientBridge bridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                        groceryDao.insertIngredientIntoGrocery(bridge);
                    }
                } else {
                    groceryDao.deleteRecipeIngredientFromGrocery(ingredient.getId(), offlineRecipe.getId(), grocery.getId());
                }

                GroceryIngredientBridge bridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                groceryDao.insertIngredientIntoGrocery(bridge);
            }
            groceryDao.deleteIngredientsNotInGroceryAnymore();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteRecipeIngredientFromGrocery(long groceryId, long recipeId, long ingredientId) {
        Runnable task = () -> {
            groceryDao.deleteRecipeIngredientFromGrocery(ingredientId, recipeId, groceryId);
            groceryDao.deleteIngredientsNotInGroceryAnymore();
            // delete the recipe relationship from grocery if no more recipe ingredients are inside the grocery
            groceryDao.deleteRecipeIfIngredientsNotInGroceryAnymore(groceryId);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteDirectIngredientFromGrocery(long groceryId, long ingredientId) {
        Runnable task = () -> {
            groceryDao.deleteIngredientsFromGroceryIngredient(groceryId, new ArrayList<Long>(){{add(ingredientId);}});
            groceryDao.deleteIngredientsNotInGroceryAnymore();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteGroceryRecipeBridge(OfflineRecipe offlineRecipe, Grocery grocery) {
        Runnable task = () -> {
            GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), offlineRecipe.getId(), 0);
            groceryDao.deleteGroceryRecipe(bridge);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    // RECIPES

    @Override
    public void deleteRecipe(Long recipeId) {
        deleteRecipes(new ArrayList<Long>(){{add(recipeId);}});
    }

    @Override
    public synchronized void deleteRecipes(List<Long> recipeIds) {
        Runnable task = () -> {
            // delete the recipe from the RecipeEntity table
            recipeDao.deleteRecipes(recipeIds);
            groceryDao.deleteIngredientsNotInGroceryAnymore();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void insertMyRecipe(OfflineRecipe offlineRecipe, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<OfflineRecipe> task = () -> {
            long id = recipeDao.insertNewMyRecipe(new RecipeEntity(offlineRecipe));
            return new OfflineRecipe.RecipeBuilder(offlineRecipe.getName()).setId(id).build();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<OfflineRecipe> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void insertFullMyRecipe(MyRecipe myRecipe, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        new Thread(() -> {
            long recipeId = recipeDao.insertNewMyRecipe(new RecipeEntity(myRecipe));
            ingredientDao.insertUpdateIngredientsIntoRecipe(myRecipe.getIngredients(), recipeId);
            List<RecipeTagEntity> tags = new ArrayList<>();
            for (RecipeTag tag : myRecipe.getRecipeTags()) tags.add(new RecipeTagEntity(0, tag.getOnlineId(), tag.getTitle()));
            recipeTagDao.insertUpdateRecipeTagsIntoRecipe(tags, recipeId);
            for (Nutrition nutrition : myRecipe.getNutritionList()) {
                nutritionDao.insertUpdateNutritionBridge(new RecipeNutritionBridge(recipeId,
                        Nutrition.getIdFromName(nutrition.getName()), nutrition.getAmount(), nutrition.getMeasurementType().getMeasurementId()));
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onResponse(new OfflineRecipe.RecipeBuilder(myRecipe.getName()).setId(recipeId).build());
            });
        }).start();
    }

    @Override
    public synchronized void fetchMyRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            if (recipeCategoryId == null) {
                return getMyRecipeWithoutCategory(sortType);
            } else {
                return getMyRecipeWithCategory(recipeCategoryId, sortType);
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeEntity>> futureTask = executorService.submit(task);

        List<OfflineRecipe> offlineRecipes = new ArrayList<>();
        List<RecipeEntity> recipeEntities = futureTask.get();
        for (RecipeEntity recipeEntity : recipeEntities) {
            offlineRecipes.add(new OfflineRecipe(recipeEntity));
        }
        callback.onResponse(offlineRecipes);
    }

    private List<RecipeEntity> getMyRecipeWithoutCategory(SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getMyRecipesSortAlphabeticalAsc();
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getMyRecipesSortAlphabeticalDesc();
            case SortType.SORT_DATE_NEW:
                return recipeDao.getMyRecipesSortDateDesc();
            default:
                // SORT_DATE_OLD, get unorganized list
                return recipeDao.getMyRecipesSortDateAsc();
        }
    }

    private List<RecipeEntity> getMyRecipeWithCategory(Long recipeCategoryId, SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getMyRecipesSortAlphabeticalAsc(recipeCategoryId);
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getMyRecipesSortAlphabeticalDesc(recipeCategoryId);
            case SortType.SORT_DATE_NEW:
                return recipeDao.getMyRecipesSortDateDesc(recipeCategoryId);
            default:
                // SORT_DATE_OLD, get unorganized list
                return recipeDao.getMyRecipesSortDateAsc(recipeCategoryId);
        }
    }

    @Override
    public synchronized void fetchLikedRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            if (recipeCategoryId == null) {
                return getLikedRecipeWithoutCategory(sortType);
            } else {
                return getLikedRecipeWithCategory(recipeCategoryId, sortType);
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeEntity>> futureTask = executorService.submit(task);

        List<OfflineRecipe> offlineRecipes = new ArrayList<>();
        List<RecipeEntity> recipeEntities = futureTask.get();
        for (RecipeEntity recipeEntity : recipeEntities) {
            offlineRecipes.add(new OfflineRecipe(recipeEntity));
        }
        callback.onResponse(offlineRecipes);
    }

    private List<RecipeEntity> getLikedRecipeWithoutCategory(SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getLikedRecipesSortAlphabeticalAsc();
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getLikedRecipesSortAlphabeticalDesc();
            case SortType.SORT_DATE_NEW:
                return recipeDao.getLikedRecipesSortDateDesc();
            default:
                // SORT_DATE_OLD, get unorganized list
                return recipeDao.getLikedRecipesSortDateAsc();
        }
    }

    private List<RecipeEntity> getLikedRecipeWithCategory(Long recipeCategoryId, SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getLikedRecipesSortAlphabeticalAsc(recipeCategoryId);
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getLikedRecipesSortAlphabeticalDesc(recipeCategoryId);
            case SortType.SORT_DATE_NEW:
                return recipeDao.getLikedRecipesSortDateDesc(recipeCategoryId);
            default:
                // SORT_DATE_OLD, get unorganized list
                return recipeDao.getLikedRecipesSortDateAsc(recipeCategoryId);
        }
    }

    @Override
    public synchronized List<OfflineRecipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> recipeDao.getAllUnCategorizedRecipes();
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeEntity>> futureTask = executorService.submit(task);
        List<OfflineRecipe> offlineRecipes = new ArrayList<>();
        List<RecipeEntity> recipeEntities = futureTask.get();
        for (RecipeEntity recipeEntity : recipeEntities) {
            offlineRecipes.add(new OfflineRecipe(recipeEntity));
        }
        return offlineRecipes;
    }

    @Override
    public synchronized void updateRecipes(ArrayList<OfflineRecipe> offlineRecipes) {
        Runnable task = () -> {
            for (OfflineRecipe recipe : offlineRecipes) {
                recipeDao.updateRecipe(new RecipeEntity(recipe));
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void updateRecipe(OfflineRecipe recipe) {
        Runnable task = () -> recipeDao.updateRecipe(new RecipeEntity(recipe));
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void fetchFullMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException {
        new Thread(() -> {
            MyRecipe myRecipe = recipeDao.getFullMyRecipe(recipeId);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(myRecipe));
        }).start();
    }

    @Override
    public synchronized void fetchFullLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<LikedRecipe> task = () -> recipeDao.getFullLikedRecipe(recipeId);
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<LikedRecipe> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void insertTagIntoRecipe(long recipeId, RecipeTag recipeTag, DbSingleCallbackWithFail<RecipeTag> callback) {
        new Thread(() -> {
            long tagId = recipeTagDao.insertUpdateTagIntoRecipe(new RecipeTagEntity(recipeTag), recipeId);
            recipeTag.setId(tagId);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(recipeTag));
        }).start();
    }

    @Override
    public synchronized void fetchRecipeTags(long recipeId, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeTag>> task = () -> {
            List<RecipeTagTuple> tuples = recipeDao.getRecipeTags(recipeId);
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (RecipeTagTuple tuple : tuples) recipeTags.add(new RecipeTag(tuple.tagId, tuple.title));
            return recipeTags;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeTag>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void deleteRecipeTagFromBridge(long recipeId, long tagId, DbCallbackEmptyResponse callback) {
        Runnable task = () -> {
            recipeTagDao.deleteRecipeTagBridge(recipeId, tagId);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    // Recipe Category

    @Override
    public void deleteRecipeCategory(long categoryId) {
        deleteRecipeCategories(new ArrayList<Long>(){{add(categoryId);}});
    }

    @Override
    public synchronized void deleteRecipeCategories(List<Long> categoryIds) {
        Runnable task = () -> recipeDao.deleteRecipeCategories(categoryIds);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<RecipeCategoryEntity>> task = () -> {
            switch (sortType.getSortKey()) {
                case SortType.SORT_ALPHABETICAL_ASC:
                    return recipeDao.getAllRecipeCategoriesSortAlphabeticalAsc();
                case SortType.SORT_ALPHABETICAL_DEC:
                    return recipeDao.getAllRecipeCategoriesSortAlphabeticalDesc();
                default: return recipeDao.getAllRecipeCategories();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeCategoryEntity>> futureTask = executorService.submit(task);
        List<RecipeCategory> recipeCategories = new ArrayList<>();
        List<RecipeCategoryEntity> recipeCategoryEntities = futureTask.get();
        for (RecipeCategoryEntity recipeCategoryEntity : recipeCategoryEntities) {
            recipeCategories.add(new RecipeCategory(recipeCategoryEntity));
        }
        callback.onResponse(recipeCategories);
    }

    @Override
    public synchronized void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException {
        Callable<RecipeCategoryEntity> task = () -> recipeDao.getRecipeCategory(recipeCategoryId);
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<RecipeCategoryEntity> futureTask = executorService.submit(task);
        RecipeCategory recipeCategory = new RecipeCategory(futureTask.get());
        recipeCategoryObserver.set(recipeCategory);
    }

    @Override
    public synchronized void addRecipeCategory(RecipeCategory recipeCategory) {
        Runnable task = () -> {
            RecipeCategoryEntity recipeCategoryEntity = new RecipeCategoryEntity(recipeCategory);
            recipeDao.insertRecipeCategory(recipeCategoryEntity);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteNutrition(long recipeId, long nutritionId) {
        Runnable task = () -> recipeDao.deleteFlagNutritionBridge(recipeId, nutritionId);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void addNutrition(long recipeId, Nutrition nutrition) {
        Runnable task = () -> nutritionDao.insertUpdateNutritionBridge(new RecipeNutritionBridge(recipeId, Nutrition.getIdFromName(nutrition.getName()), nutrition.getAmount(), nutrition.getMeasurementType().getMeasurementId()));
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void addIngredient(Ingredient ingredient, DbSingleCallback<Ingredient> callback) {
        new Thread(() -> {
            long id = ingredientDao.insertUpdateIngredient(new IngredientEntity(ingredient));
            ingredient.setId(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(ingredient));
        }).start();
    }

    @Override
    public synchronized void addTag(RecipeTag tag, DbSingleCallback<RecipeTag> callback) {
        new Thread(() -> {
            long id = recipeTagDao.insertUpdateRecipeTag(new RecipeTagEntity(tag));
            tag.setId(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(tag));
        }).start();
    }

    @Override
    public synchronized void insertUpdateIngredientsIntoGrocery(long groceryId, Ingredient ingredient, DbSingleCallbackWithFail<Ingredient> callback) {
        new Thread(() -> {
            // add or update the Ingredient
            long id = ingredientDao.insertUpdateIngredient(new IngredientEntity(ingredient));
            ingredient.setId(id);
            // insert into bridge
            GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryId,
                    ingredient.getId(), false);
            groceryDao.insertIntoBridge(groceryIngredientBridge);
            // insert into GroceryIngredientEntity
            GroceryIngredientEntity groceryIngredientEntity = new GroceryIngredientEntity(groceryId,
                    ingredient.getId(), ingredient.getQuantity(), ingredient.getQuantityMeasId());
            groceryDao.insertGroceryIngredient(groceryIngredientEntity);
            // TODO: retrieve the full groceryIngredient using tuples to prevent overriding with old ingredient (see insertUpdateIngredientsIntoRecipe())
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(ingredient));
        }).start();
    }

    @Override
    public synchronized void insertUpdateIngredientsIntoRecipe(long recipeId, Ingredient ingredient, DbSingleCallbackWithFail<Ingredient> callback) {
        new Thread(() -> {
            long ingredientId = ingredientDao.insertUpdateIngredientIntoRecipe(ingredient, recipeId);
            // retrieve the important ingredient values to prevent overriding the recipe
            RecipeIngredientsTuple tuple = ingredientDao.getFullRecipeIngredientById(ingredientId, recipeId);
            Ingredient addedIngredient = new Ingredient.IngredientBuilder(tuple.ingredientId, tuple.ingredientName)
                    .setQuantity(tuple.quantity)
                    .setQuantityMeasId(tuple.quantityMeasId)
                    .setFoodType(tuple.foodTypeId)
                    .build();
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(addedIngredient));
        }).start();
    }

    @Override
    public void deleteIngredient(long ingredientId) {
        deleteIngredients(new ArrayList<Long>(){{add(ingredientId);}});
    }

    @Override
    public synchronized void deleteIngredients(List<Long> ingredientIds) {
        Runnable task = () -> ingredientDao.deleteIngredient(ingredientIds);
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteIngredientsFromGrocery(long groceryId, List<Long> ingredientIds) {
        Runnable task = () -> {
            groceryDao.deleteIngredientsFromGroceryIngredient(groceryId, ingredientIds);
            groceryDao.deleteGroceryIngredientsFromGroceryRecipeIngredientEntity(groceryId, ingredientIds);
            groceryDao.deleteIngredientsNotInGroceryAnymore();
            // delete the recipe relationship from grocery if no more recipe ingredients are inside the grocery
            groceryDao.deleteRecipeIfIngredientsNotInGroceryAnymore(groceryId);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void deleteIngredientsFromRecipe(long recipeId, List<Long> ingredientIds) {
        Runnable task = () -> {
            recipeDao.deleteFlagIngredientsFromRecipeIngredientBridge(recipeId, ingredientIds);
            recipeDao.deleteRecipeIngredientsFromGroceryRecipeIngredientEntity(recipeId, ingredientIds);
            groceryDao.deleteIngredientsNotInGroceryAnymore();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void fetchIngredientsFromRecipe(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            List<Ingredient> ingredients = new ArrayList<>();
            List<RecipeIngredientsTuple> tuples;

            switch (sortType.getSortKey()) {
                case SortType.SORT_ALPHABETICAL_ASC:
                    tuples = recipeDao.getRecipeIngredientsSortAlphabeticalAsc(offlineRecipe.getId());
                    break;
                case SortType.SORT_ALPHABETICAL_DEC:
                    tuples = recipeDao.getRecipeIngredientsSortAlphabeticalDesc(offlineRecipe.getId());
                    break;
                default:
                    tuples = recipeDao.getRecipeIngredients(offlineRecipe.getId());
            }

            for (int i = 0; i < tuples.size(); i++) {
                RecipeIngredientsTuple tuple = tuples.get(i);
                ingredients.add(new Ingredient.IngredientBuilder(tuple.ingredientId, tuple.onlineIngredientId, tuple.ingredientName)
                        .setQuantity(tuple.quantity)
                        .setQuantityMeasId(tuple.quantityMeasId)
                        .setFoodType(tuple.foodTypeId)
                        .setDateUpdated(tuple.dateUpdated)
                        .setDateSynchronized(tuple.dateSynchronized)
                        .build());
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        List<Ingredient> ingredients = futureTask.get();
        callback.onResponse(ingredients);
    }


    @Override
    public synchronized void fetchIngredients(DbCallback<Ingredient> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            List<Ingredient> ingredients = new ArrayList<>();

            List<IngredientEntity> ingredientEntities;
            switch (sortType.getSortKey()) {
                case SortType.SORT_ALPHABETICAL_ASC:
                    ingredientEntities = ingredientDao.getIngredientsSortAlphabeticalAsc();
                    break;
                case SortType.SORT_ALPHABETICAL_DEC:
                    ingredientEntities = ingredientDao.getIngredientsSortAlphabeticalDesc();
                    break;
                default:
                    ingredientEntities = ingredientDao.getIngredients();
            }
            for (int i = 0; i < ingredientEntities.size(); i++) {
                IngredientEntity ingredientEntity = ingredientEntities.get(i);
                ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getOnlineIngredientId(), ingredientEntity.getIngredientName())
                        .setFoodType(ingredientEntity.getFoodTypeId())
                        .build());
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void deleteRecipeFromGrocery(Grocery grocery, OfflineRecipe offlineRecipe) {
        Runnable task = () -> {
            // delete recipe and grocery association in GroceryRecipeBridge
            GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), offlineRecipe.getId(), 0);
            groceryDao.deleteGroceryRecipe(bridge);
            // delete ingredients associated with recipe and grocery added to GroceryRecipeIngredientEntity
            recipeDao.deleteRecipeGroceryFromGroceryRecipeIngredient(grocery.getId(), offlineRecipe.getId());
            groceryDao.deleteIngredientsNotInGroceryAnymore();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void fetchRecipeIngredientsInGrocery(Grocery grocery, OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
            List<RecipeIngredientsWithGroceryCheckTuple> tuples = recipeDao.getRecipeIngredientsInGrocery(offlineRecipe.getId(), grocery.getId());
            for (RecipeIngredientsWithGroceryCheckTuple tuple : tuples) {
                Ingredient ingredient = new Ingredient.IngredientBuilder(tuple.ingredientId, tuple.ingredientName)
                        .setQuantity(tuple.quantity)
                        .setQuantityMeasId(tuple.quantityMeasId)
                        .setFoodType(tuple.foodTypeId)
                        .setDateSynchronized(tuple.dateSynchronized)
                        .setDateUpdated(tuple.dateUpdated)
                        .build();
                IngredientWithGroceryCheck ingredientWithGroceryCheck = new IngredientWithGroceryCheck(ingredient, tuple.groceryId != 0);
                ingredients.add(ingredientWithGroceryCheck);
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<IngredientWithGroceryCheck>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void fetchRecipeIngredientsNotInGrocery(OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
            List<RecipeIngredientsTuple> tuples = recipeDao.getRecipeIngredients(offlineRecipe.getId());
            for (RecipeIngredientsTuple tuple : tuples) {
                Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.onlineIngredientId, tuple.ingredientName, tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), tuple.dateUpdated, tuple.dateSynchronized);
                IngredientWithGroceryCheck ingredientWithGroceryCheck = new IngredientWithGroceryCheck(ingredient, false);
                ingredients.add(ingredientWithGroceryCheck);
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<IngredientWithGroceryCheck>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void fetchIngredientsNotInRecipe(OfflineRecipe offlineRecipe, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        fetchIngredientsNotInRecipeSearch(offlineRecipe, callback, "");
    }

    @Override
    public void fetchIngredientsNotInRecipeSearch(OfflineRecipe offlineRecipe, DbCallback<Ingredient> callback, String search) throws ExecutionException, InterruptedException {
        new Thread(() -> {
            List<Ingredient> ingredients = new ArrayList<>();
            List<IngredientEntity> ingredientEntities = recipeDao.getIngredientsNotInRecipe(offlineRecipe.getId(), "%" + search + "%");
            for (int i = 0; i < ingredientEntities.size(); i++) {
                IngredientEntity ingredientEntity = ingredientEntities.get(i);
                ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getOnlineIngredientId(), ingredientEntity.getIngredientName())
                        .setFoodType(ingredientEntity.getFoodTypeId())
                        .build());
            }
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(ingredients));
        }).start();
    }

    @Override
    public synchronized void fetchIngredientsNotInGrocery(Grocery grocery, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        fetchIngredientsNotInGrocerySearch(grocery, callback, "");
    }

    @Override
    public synchronized void fetchIngredientsNotInGrocerySearch(Grocery grocery, DbCallback<Ingredient> callback, String search) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            List<Ingredient> ingredients = new ArrayList<>();
            List<IngredientEntity> ingredientEntities = groceryDao.getIngredientsNotInGrocery(grocery.getId(), "%" + search + "%");
            for (int i = 0; i < ingredientEntities.size(); i++) {
                IngredientEntity ingredientEntity = ingredientEntities.get(i);
                ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getOnlineIngredientId(), ingredientEntity.getIngredientName())
                        .setFoodType(ingredientEntity.getFoodTypeId())
                        .build());
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void fetchGroceryIngredients(Grocery grocery, SortType sortType, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryIngredient>> task = () -> {
            // TODO: Add sorting functionality
            // get the ingredients directly added to the grocery list
            List<GroceryIngredientsTuple> tupleGrocery = groceryDao.getGroceryIngredients(grocery.getId());
            // get the recipe ingredients added to the grocery list
            List<RecipeIngredientInGroceryTuple> tupleRecipe = groceryDao.getRecipeIngredientsInGrocery(grocery.getId());
            // combine the two lists to show all Grocery ingredients
            return combineGroceryAndRecipeIngredients(tupleGrocery, tupleRecipe);
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryIngredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    /**
     * Combines the queries grocery ingredients and recipe ingredients added to a grocery list.
     * @return  All Ingredients, added directly or through a recipe, inside a grocery list
     */
    private List<GroceryIngredient> combineGroceryAndRecipeIngredients(List<GroceryIngredientsTuple> tupleGrocery, List<RecipeIngredientInGroceryTuple> tupleRecipe) {
        // keeps track of all ingredients inside the grocery list
        Map<Long, GroceryIngredient> map = new LinkedHashMap<>();
        // retrieve the Grocery ingredients added directly
        for (GroceryIngredientsTuple tuple : tupleGrocery) {
            // todo: change dateUpdated and dateSynchronized values to when grocery implements online functionality
            Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.onlineIngredientId, tuple.ingredientName,
                    tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), null, null);
            GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked, true);
            map.put(tuple.ingredientId, groceryIngredient);
        }
        // retrieve the recipe ingredients added to the grocery
        for (RecipeIngredientInGroceryTuple tuple : tupleRecipe) {
            if (!map.containsKey(tuple.ingredientId)) {
                Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.onlineIngredientId, tuple.ingredientName,
                        tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), tuple.dateUpdated, tuple.dateSynchronized);
                GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked, false);
                map.put(tuple.ingredientId, groceryIngredient);
            }
            GroceryIngredient groceryIngredient = map.get(tuple.ingredientId);
            RecipeWithIngredient recipeWithIngredient = new RecipeWithIngredient(tuple.recipeId, tuple.onlineRecipeId,
                    tuple.recipeName, tuple.amount, tuple.quantity, tuple.quantityMeasId);
            groceryIngredient.addRecipe(recipeWithIngredient);
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public synchronized void updateGroceryIngredientChecked(Grocery grocery, GroceryIngredient groceryIngredient) {
        Runnable task = () -> groceryDao.updateGroceryIngredientChecked(new GroceryIngredientBridge(grocery.getId(), groceryIngredient.getId(), groceryIngredient.getIsChecked()));
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public synchronized void searchGroceries(String grocerySearch, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        Callable<List<Grocery>> task = () -> {
            List<GroceryEntity> groceryEntities = groceryDao.searchGroceries("%" + grocerySearch + "%");
            List<Grocery> groceries = new ArrayList<>();
            for (GroceryEntity groceryEntity : groceryEntities) groceries.add(new Grocery(groceryEntity));
            return groceries;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Grocery>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchGroceryIngredients(long groceryId, String ingredientSearch, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryIngredient>> task = () -> {
            // get the ingredients directly added to the grocery list
            List<GroceryIngredientsTuple> tupleGrocery = groceryDao.searchGroceryIngredients(groceryId, "%"+ingredientSearch+"%");
            // get the recipe ingredients added to the grocery list
            List<RecipeIngredientInGroceryTuple> tupleRecipe = groceryDao.searchRecipeIngredientsInGrocery(groceryId, "%"+ingredientSearch+"%");
            // combine the two lists to show all Grocery ingredients
            return combineGroceryAndRecipeIngredients(tupleGrocery, tupleRecipe);
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryIngredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchMyRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            List<RecipeEntity> recipeEntities = recipeDao.searchMyRecipes("%" + recipeSearch + "%");
            List<OfflineRecipe> offlineRecipes = new ArrayList<>();
            for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
            return offlineRecipes;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchLikedRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            List<RecipeEntity> recipeEntities = recipeDao.searchLikedRecipes("%" + recipeSearch + "%");
            List<OfflineRecipe> offlineRecipes = new ArrayList<>();
            for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
            return offlineRecipes;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchMyRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            List<RecipeEntity> recipeEntities = recipeDao.searchMyRecipesInCategory(categoryId, "%" + recipeSearch + "%");
            List<OfflineRecipe> offlineRecipes = new ArrayList<>();
            for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
            return offlineRecipes;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchLikedRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            List<RecipeEntity> recipeEntities = recipeDao.searchLikedRecipesInCategory(categoryId, "%" + recipeSearch + "%");
            List<OfflineRecipe> offlineRecipes = new ArrayList<>();
            for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
            return offlineRecipes;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchRecipeCategories(String categorySearch, DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeCategory>> task = () -> {
            List<RecipeCategoryEntity> recipeCategoryEntities = recipeDao.searchRecipeCategories("%" + categorySearch + "%");
            List<RecipeCategory> recipeCategories = new ArrayList<>();
            for (RecipeCategoryEntity recipeCategoryEntity : recipeCategoryEntities) recipeCategories.add(new RecipeCategory(recipeCategoryEntity));
            return recipeCategories;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeCategory>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchRecipeIngredients(long recipeId, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            List<RecipeIngredientsTuple> recipeIngredientsTuples = recipeDao.searchRecipeIngredients(recipeId,"%" + ingredientSearch + "%");
            List<Ingredient> ingredients = new ArrayList<>();
            for (RecipeIngredientsTuple tuple : recipeIngredientsTuples) ingredients.add(new Ingredient(tuple.ingredientId, tuple.onlineRecipeId,
                    tuple.ingredientName, tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), tuple.dateUpdated, tuple.dateSynchronized));
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void searchIngredients(String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            List<IngredientEntity> ingredientEntities = ingredientDao.searchIngredients("%" + ingredientSearch + "%");
            List<Ingredient> ingredients = new ArrayList<>();
            for (IngredientEntity entity : ingredientEntities) {
                ingredients.add(new Ingredient.IngredientBuilder(entity.getIngredientId(), entity.getOnlineIngredientId(), entity.getIngredientName())
                        .setFoodType(entity.getFoodTypeId())
                        .build());
            }
            return ingredients;
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public synchronized void syncMyRecipeUpdate(RecipeEntity recipeEntity, long accessLevelId) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        recipeDao.syncMyRecipeUpdate(recipeEntity.getRecipeId(), recipeEntity.getName(), recipeEntity.getDescription(), recipeEntity.getPrepTime(),
                recipeEntity.getCookTime(), recipeEntity.getServingSize(), recipeEntity.getInstructions(), recipeEntity.getDateSynchronized(), accessLevelId);
    }

    @Override
    public void syncLikedRecipeUpdate(RecipeEntity recipeEntity) {
        // TODO:
    }

    @Override
    public synchronized long syncMyRecipeInsert(RecipeEntity recipeEntity, long accessLevelId) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        return recipeDao.syncMyRecipeInsert(recipeEntity.getOnlineRecipeId(), recipeEntity.getName(), recipeEntity.getDescription(), recipeEntity.getInstructions(),
                recipeEntity.getPrepTime(), recipeEntity.getCookTime(), recipeEntity.getServingSize(), recipeEntity.getDateSynchronized(), accessLevelId);
    }

    @Override
    public synchronized long syncLikedRecipeInsert(RecipeEntity recipeEntity) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        return recipeDao.syncLikedRecipeInsert(recipeEntity);
    }

    @Override
    public synchronized void syncRecipeUpdateSynchronized(long recipeId, Long onlineRecipeId, Timestamp dateSync) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (onlineRecipeId == null) {
            // update dateSynchronized only after updating online
            recipeDao.syncRecipeUpdateDateSync(recipeId, dateSync);
        } else {
            // update dateSynchronized and onlineId after inserting online
            recipeDao.syncRecipeInsertedOnline(recipeId, onlineRecipeId, dateSync);
        }
    }

    @Override
    public synchronized void syncIngredientUpdateInsert(IngredientEntity ingredientEntity, long recipeId, long ingredientId, float quantity, Long measurementId, Timestamp dateSynchronized, boolean isDeleted, long foodType, SyncJSON.OnlineUpdateIdentifier identifier) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (identifier == SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE) {
            RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeId, ingredientId, quantity, measurementId);
            recipeIngredientBridge.setDateSynchronized(dateSynchronized);
            recipeIngredientBridge.setIsDeleted(isDeleted);
            ingredientDao.syncIngredientInsert(ingredientEntity, recipeIngredientBridge);
        } else if (identifier == SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE) {
            ingredientDao.syncIngredientUpdate(ingredientEntity, recipeId, ingredientId, quantity, measurementId, dateSynchronized, isDeleted);
        } else {
            throw new IllegalArgumentException(getWrongSyncUpdateInsertIdentifier(identifier));
        }
    }

    @Override
    public synchronized void syncIngredientUpdateSynchronized(long recipeId, long ingredientId, Long onlineIngredientId, Timestamp dateSync) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (onlineIngredientId == null) {
            ingredientDao.syncIngredientUpdateDateSync(recipeId, ingredientId, dateSync);
        } else {
            ingredientDao.syncIngredientInsertedOnline(recipeId, ingredientId, onlineIngredientId, dateSync);
        }
    }

    @Override
    public synchronized void syncRecipeTagUpdateInsert(RecipeTagEntity recipeTagEntity, long recipeId, long tagId, Timestamp dateSynchronized, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (identifier == SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE) {
            RecipeTagBridge recipeTagBridge = new RecipeTagBridge(recipeId, tagId);
            recipeTagBridge.setDateSynchronized(dateSynchronized);
            recipeTagBridge.setIsDeleted(isDeleted);
            recipeTagDao.syncTagInsert(recipeTagEntity, recipeTagBridge);
        } else if (identifier == SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE) {
            recipeTagDao.syncTagUpdate(recipeTagEntity, recipeId, tagId, dateSynchronized, isDeleted);
        } else {
            throw new IllegalArgumentException(getWrongSyncUpdateInsertIdentifier(identifier));
        }
    }

    @Override
    public synchronized void syncRecipeTagUpdateSynchronized(long recipeId, long tagId, Long onlineTagId, Timestamp dateSync) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (onlineTagId == null) {
            // update dateSynchronized only after updating online
            recipeTagDao.syncTagUpdateDateSync(recipeId, tagId, dateSync);
        } else {
            // update dateSynchronized and onlineId after inserting online
            recipeTagDao.syncTagInsertedOnline(recipeId, tagId, onlineTagId, dateSync);
        }
    }

    @Override
    public synchronized void syncNutritionUpdateInsert(long recipeId, long nutritionId, int amount, Long measurementId, Timestamp dateSynchronized, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        if (identifier == SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE) {
            RecipeNutritionBridge recipeNutritionBridge = new RecipeNutritionBridge(recipeId, nutritionId, amount, measurementId);
            recipeNutritionBridge.setDateSynchronized(dateSynchronized);
            recipeNutritionBridge.setIsDeleted(isDeleted);
            nutritionDao.syncNutritionBridgeInsert(recipeNutritionBridge);
        } else if (identifier == SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE) {
            nutritionDao.syncNutritionBridgeUpdate(recipeId, nutritionId, amount, measurementId, dateSynchronized, isDeleted);
        } else {
            throw new IllegalArgumentException(getWrongSyncUpdateInsertIdentifier(identifier));
        }
    }

    @Override
    public synchronized void syncNutritionUpdateSynchronized(long recipeId, long nutritionId, Timestamp dateSync) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            throw new IllegalStateException("Methods needs to be run on a background thread");
        nutritionDao.syncNutritionUpdateDateSync(recipeId, nutritionId, dateSync);
    }

    private String getWrongSyncUpdateInsertIdentifier(SyncJSON.OnlineUpdateIdentifier identifier) {
        return "identifier " + identifier + " must be either " + SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE + " or " + SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE;
    }
}
