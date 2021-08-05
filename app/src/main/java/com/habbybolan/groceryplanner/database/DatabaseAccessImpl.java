package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.DbSingleCallback;
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
import com.habbybolan.groceryplanner.database.entities.RecipeNutritionBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.IngredientWithGroceryCheck;
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
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseAccessImpl implements DatabaseAccess {

    private GroceryDao groceryDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;

    private Lock groceryTableLock = new ReentrantLock();
    private Lock ingredientTableLock = new ReentrantLock();
    private Lock recipeTableLock = new ReentrantLock();
    private Lock recipeCategoryLock = new ReentrantLock();

    public DatabaseAccessImpl(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao) {
        this.groceryDao = groceryDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
    }

    // GROCERIES

    @Override
    public void deleteGrocery(Long groceryId) {
        deleteGroceries(new ArrayList<Long>(){{add(groceryId);}});
    }

    @Override
    public void deleteGroceries(List<Long> groceryIds) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();

                // delete the grocery
                groceryDao.deleteGroceries(groceryIds);

            } finally {
                ingredientTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void addGrocery(Grocery grocery) {
        GroceryEntity groceryEntity = new GroceryEntity(grocery);
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                groceryDao.insertGrocery(groceryEntity);
            } finally {
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchGroceries(DbCallback<Grocery> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> {
            try {
                groceryTableLock.lock();

                switch (sortType.getSortKey()) {
                    case SortType.SORT_ALPHABETICAL_ASC:
                        return groceryDao.getAllGroceriesSortAlphabeticalAsc();
                    case SortType.SORT_ALPHABETICAL_DEC:
                        return groceryDao.getAllGroceriesSortAlphabeticalDesc();
                    default:
                        return groceryDao.getAllGroceries();
                }
            } finally {
                groceryTableLock.unlock();
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
    public void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryRecipe>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                List<RecipeGroceriesTuple> groceries = recipeDao.getGroceriesHoldingRecipe(offlineRecipe.getId());
                List<GroceryRecipe> groceryRecipes = new ArrayList<>();
                for (RecipeGroceriesTuple grocery : groceries) {
                    groceryRecipes.add(new GroceryRecipe( grocery.groceryName, grocery.groceryId, grocery.onlineGroceryId, grocery.amount, grocery.dateSynchronized));
                }
                return groceryRecipes;
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryRecipe>> futureTask = executorService.submit(task);
        List<GroceryRecipe> groceryRecipes = futureTask.get();
        callback.onResponse(groceryRecipes);
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();

                return recipeDao.getGroceriesNotHoldingRecipe(offlineRecipe.getId());
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
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
    public void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();

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
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteRecipeIngredientFromGrocery(long groceryId, long recipeId, long ingredientId) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();

                groceryDao.deleteRecipeIngredientFromGrocery(ingredientId, recipeId, groceryId);
                groceryDao.deleteIngredientsNotInGroceryAnymore();
                // delete the recipe relationship from grocery if no more recipe ingredients are inside the grocery
                groceryDao.deleteRecipeIfIngredientsNotInGroceryAnymore(groceryId);
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteDirectIngredientFromGrocery(long groceryId, long ingredientId) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();

                groceryDao.deleteIngredientsFromGroceryIngredient(groceryId, new ArrayList<Long>(){{add(ingredientId);}});
                groceryDao.deleteIngredientsNotInGroceryAnymore();
            } finally {
                groceryTableLock.unlock();
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteGroceryRecipeBridge(OfflineRecipe offlineRecipe, Grocery grocery) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), offlineRecipe.getId(), 0);
                groceryDao.deleteGroceryRecipe(bridge);
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
            }
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
    public void deleteRecipes(List<Long> recipeIds) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();

                // delete the recipe from the RecipeEntity table
                recipeDao.deleteRecipes(recipeIds);
                groceryDao.deleteIngredientsNotInGroceryAnymore();

            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void addRecipe(OfflineRecipe offlineRecipe) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeDao.insertNewMyRecipe(new RecipeEntity(offlineRecipe));
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipes(Long recipeCategoryId, SortType sortType, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();
                if (recipeCategoryId == null) {
                    return getRecipeWithoutCategory(sortType);
                } else {
                    return getRecipeWithCategory(recipeCategoryId, sortType);
                }
            } finally {
                recipeCategoryLock.unlock();
                recipeTableLock.unlock();
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

    private List<RecipeEntity> getRecipeWithoutCategory(SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getAllRecipesSortAlphabeticalAsc();
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getAllRecipesSortAlphabeticalDesc();
            case SortType.SORT_DATE_NEW:
                return recipeDao.getAllRecipesSortDateDesc();
            case SortType.SORT_DATE_OLD:
                return recipeDao.getAllRecipesSortDateAsc();
            default:
                // SORT_NONE, get unorganized list
                return recipeDao.getAllRecipes();
        }
    }

    private List<RecipeEntity> getRecipeWithCategory(Long recipeCategoryId, SortType sortType) {
        switch (sortType.getSortKey()) {
            case SortType.SORT_ALPHABETICAL_ASC:
                return recipeDao.getAllRecipesSortAlphabeticalAsc(recipeCategoryId);
            case SortType.SORT_ALPHABETICAL_DEC:
                return recipeDao.getAllRecipesSortAlphabeticalDesc(recipeCategoryId);
            case SortType.SORT_DATE_NEW:
                return recipeDao.getAllRecipesSortDateDesc(recipeCategoryId);
            case SortType.SORT_DATE_OLD:
                return recipeDao.getAllRecipesSortDateAsc(recipeCategoryId);
            default:
                // SORT_NONE, get unorganized list
                return recipeDao.getAllRecipes(recipeCategoryId);
        }
    }

    @Override
    public List<OfflineRecipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            try {
                return recipeDao.getAllUnCategorizedRecipes();
            } finally {
                recipeCategoryLock.unlock();
                recipeTableLock.unlock();
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
        return offlineRecipes;
    }

    @Override
    public void updateRecipes(ArrayList<OfflineRecipe> offlineRecipes) {
        Runnable task = () -> {
            recipeTableLock.lock();
            try {
                for (OfflineRecipe recipe : offlineRecipes) {
                    recipeDao.updateRecipe(new RecipeEntity(recipe));
                }
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeDao.updateRecipe(new RecipeEntity(offlineRecipe));
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipe(long recipeId, DbSingleCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<OfflineRecipe> task = () -> {
            try {
                recipeTableLock.lock();
                return recipeDao.getFullRecipe(recipeId);
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<OfflineRecipe> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void addRecipeTag(long recipeId, String title) {
        addRecipeTags(recipeId, new ArrayList<String>(){{add(title);}});
    }

    @Override
    public void addRecipeTags(long recipeId, List<String> titles) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                List<RecipeTagEntity> tags = new ArrayList<>();
                for (String title : titles) tags.add(new RecipeTagEntity(0, null, title));
                    recipeDao.insertUpdateRecipeTagsIntoBridge(tags, recipeId);
            } finally {
                recipeTableLock.unlock();
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipeTags(long recipeId, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeTag>> task = () -> {
            try {
                recipeTableLock.lock();
                List<RecipeTagTuple> tuples = recipeDao.getRecipeTags(recipeId);
                List<RecipeTag> recipeTags = new ArrayList<>();
                for (RecipeTagTuple tuple : tuples) recipeTags.add(new RecipeTag(tuple.tagId, tuple.title));
                return recipeTags;
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeTag>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void deleteRecipeTagFromBridge(long recipeId, RecipeTag recipeTag) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                if (recipeTag.getId() != 0) {
                    recipeDao.deleteFlagRecipeTagBridge(recipeId, recipeTag.getId());
                } else {
                    recipeDao.deleteFlagRecipeTagBridgeByTitle(recipeId, recipeTag.getTitle());
                }
            } finally {
                recipeTableLock.unlock();
            }
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
    public void deleteRecipeCategories(List<Long> categoryIds) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();

                recipeDao.deleteRecipeCategories(categoryIds);
            } finally {
                recipeCategoryLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipeCategories(DbCallback<RecipeCategory> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<RecipeCategoryEntity>> task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();
                switch (sortType.getSortKey()) {
                    case SortType.SORT_ALPHABETICAL_ASC:
                        return recipeDao.getAllRecipeCategoriesSortAlphabeticalAsc();
                    case SortType.SORT_ALPHABETICAL_DEC:
                        return recipeDao.getAllRecipeCategoriesSortAlphabeticalDesc();
                    default: return recipeDao.getAllRecipeCategories();
                }
            } finally {
                recipeTableLock.unlock();
                recipeCategoryLock.unlock();
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
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException {
        Callable<RecipeCategoryEntity> task = () -> {
            try {
                recipeCategoryLock.lock();
                return recipeDao.getRecipeCategory(recipeCategoryId);
            } finally {
                recipeCategoryLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<RecipeCategoryEntity> futureTask = executorService.submit(task);
        RecipeCategory recipeCategory = new RecipeCategory(futureTask.get());
        recipeCategoryObserver.set(recipeCategory);
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        Runnable task = () -> {
            try {
                recipeCategoryLock.lock();
                RecipeCategoryEntity recipeCategoryEntity = new RecipeCategoryEntity(recipeCategory);
                recipeDao.insertRecipeCategory(recipeCategoryEntity);
            } finally {
                recipeCategoryLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteNutrition(long recipeId, long nutritionId) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeDao.deleteFlagNutritionBridge(recipeId, nutritionId);
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void addNutrition(long recipeId, Nutrition nutrition) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeDao.insertUpdateNutritionBridge(new RecipeNutritionBridge(recipeId, Nutrition.getIdFromFrom(nutrition.getName()), nutrition.getAmount(), nutrition.getMeasurementId()));
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Helper for adding or updating an ingredient. Sets the id of the Ingredient if updating.
     * @param ingredient    Ingredient to add or update.
     */
    private void addIngredientHelper(Ingredient ingredient) {
        // todo: remove and put functionality inside RecipeDao
        // if no explicit id set, check if that ingredient already exists
        if (ingredient.getId() == 0) {
            long id = ingredientDao.insertIngredient(new IngredientEntity(ingredient));
            // ingredient already exists
            if (id == -1) {
                IngredientEntity ingredientEntity = ingredientDao.getIngredient(ingredient.getName());
                id = ingredientEntity.getIngredientId();
                ingredient.setId(id);
                ingredientDao.updateIngredient(new IngredientEntity(ingredient));
                // otherwise new ingredient created
            } else {
                ingredient.setId(id);
            }
            // otherwise id is set, so update the ingredient
        } else {
            ingredientDao.updateIngredient(new IngredientEntity(ingredient));
        }
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        Runnable task = () -> {
            try {
                ingredientTableLock.lock();
                ingredientDao.insertUpdateIngredient(new IngredientEntity(ingredient));
            } finally {
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void insertIngredientsIntoGrocery(long groceryId, List<Ingredient> ingredients) {
        Runnable task = () -> {
            try {
                // todo: parallel insertIngredientsIntoRecipe
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    // add or update the Ingredient
                    addIngredientHelper(ingredient);
                    // insert into bridge
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryId,
                            ingredient.getId(), false);
                    groceryDao.insertIntoBridge(groceryIngredientBridge);
                    // insert into GroceryIngredientEntity
                    GroceryIngredientEntity groceryIngredientEntity = new GroceryIngredientEntity(groceryId,
                            ingredient.getId(), ingredient.getQuantity(), ingredient.getQuantityMeasId());
                    groceryDao.insertGroceryIngredient(groceryIngredientEntity);
                }
            } finally {
                ingredientTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void insertIngredientsIntoRecipe(long recipeId, List<Ingredient> ingredients) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                recipeDao.insertUpdateIngredientsIntoRecipe(ingredients, recipeId, ingredientDao);
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredient(long ingredientId) {
        deleteIngredients(new ArrayList<Long>(){{add(ingredientId);}});
    }

    @Override
    public void deleteIngredients(List<Long> ingredientIds) {
        Runnable task = () -> {
            try {
                ingredientTableLock.lock();
                // delete the ingredient
                ingredientDao.deleteIngredient(ingredientIds);

            } finally {
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredientsFromGrocery(long groceryId, List<Long> ingredientIds) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();

                groceryDao.deleteIngredientsFromGroceryIngredient(groceryId, ingredientIds);
                groceryDao.deleteGroceryIngredientsFromGroceryRecipeIngredientEntity(groceryId, ingredientIds);
                groceryDao.deleteIngredientsNotInGroceryAnymore();
                // delete the recipe relationship from grocery if no more recipe ingredients are inside the grocery
                groceryDao.deleteRecipeIfIngredientsNotInGroceryAnymore(groceryId);
            } finally {
                ingredientTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredientsFromRecipe(long recipeId, List<Long> ingredientIds) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();

                recipeDao.deleteFlagIngredientsFromRecipeIngredientBridge(recipeId, ingredientIds);
                recipeDao.deleteRecipeIngredientsFromGroceryRecipeIngredientEntity(recipeId, ingredientIds);
                groceryDao.deleteIngredientsNotInGroceryAnymore();
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchIngredientsFromRecipe(OfflineRecipe offlineRecipe, SortType sortType, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();

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
                            .build());
                }
                return ingredients;
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        List<Ingredient> ingredients = futureTask.get();
        callback.onResponse(ingredients);
    }


    @Override
    public void fetchIngredients(DbCallback<Ingredient> callback, SortType sortType) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
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
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void deleteRecipeFromGrocery(Grocery grocery, OfflineRecipe offlineRecipe) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                // delete recipe and grocery association in GroceryRecipeBridge
                GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), offlineRecipe.getId(), 0);
                groceryDao.deleteGroceryRecipe(bridge);
                // delete ingredients associated with recipe and grocery added to GroceryRecipeIngredientEntity
                recipeDao.deleteRecipeGroceryFromGroceryRecipeIngredient(grocery.getId(), offlineRecipe.getId());
                groceryDao.deleteIngredientsNotInGroceryAnymore();
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipeIngredientsInGrocery(Grocery grocery, OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
                List<RecipeIngredientsWithGroceryCheckTuple> tuples = recipeDao.getRecipeIngredientsInGrocery(offlineRecipe.getId(), grocery.getId());
                for (RecipeIngredientsWithGroceryCheckTuple tuple : tuples) {
                    Ingredient ingredient = new Ingredient.IngredientBuilder(tuple.ingredientId, tuple.ingredientName)
                            .setQuantity(tuple.quantity)
                            .setQuantityMeasId(tuple.quantityMeasId)
                            .setFoodType(tuple.foodTypeId)
                            .build();
                    IngredientWithGroceryCheck ingredientWithGroceryCheck = new IngredientWithGroceryCheck(ingredient, tuple.groceryId != 0);
                    ingredients.add(ingredientWithGroceryCheck);
                }
                return ingredients;
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
                ingredientTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<IngredientWithGroceryCheck>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void fetchRecipeIngredientsNotInGrocery(OfflineRecipe offlineRecipe, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
                List<RecipeIngredientsTuple> tuples = recipeDao.getRecipeIngredients(offlineRecipe.getId());
                for (RecipeIngredientsTuple tuple : tuples) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.onlineIngredientId, tuple.ingredientName, tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), tuple.dateUpdated, tuple.dateSynchronized);
                    IngredientWithGroceryCheck ingredientWithGroceryCheck = new IngredientWithGroceryCheck(ingredient, false);
                    ingredients.add(ingredientWithGroceryCheck);
                }
                return ingredients;
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
                ingredientTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<IngredientWithGroceryCheck>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void fetchIngredientsNotInRecipe(OfflineRecipe offlineRecipe, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = recipeDao.getIngredientsNotInRecipe(offlineRecipe.getId());
                for (int i = 0; i < ingredientEntities.size(); i++) {
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getOnlineIngredientId(), ingredientEntity.getIngredientName())
                            .setFoodType(ingredientEntity.getFoodTypeId())
                            .build());
                }
                return ingredients;
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void fetchIngredientsNotInGrocery(Grocery grocery, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = groceryDao.getIngredientsNotInGrocery(grocery.getId());
                for (int i = 0; i < ingredientEntities.size(); i++) {
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getOnlineIngredientId(), ingredientEntity.getIngredientName())
                            .setFoodType(ingredientEntity.getFoodTypeId())
                            .build());
                }
                return ingredients;
            } finally {
                ingredientTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void fetchGroceryIngredients(Grocery grocery, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryIngredient>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();

                // get the ingredients directly added to the grocery list
                List<GroceryIngredientsTuple> tupleGrocery = groceryDao.getGroceryIngredients(grocery.getId());
                // get the recipe ingredients added to the grocery list
                List<RecipeIngredientInGroceryTuple> tupleRecipe = groceryDao.getRecipeIngredientsInGrocery(grocery.getId());
                // combine the two lists to show all Grocery ingredients
                return combineGroceryAndRecipeIngredients(tupleGrocery, tupleRecipe);
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
                groceryTableLock.unlock();
            }
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
    public void updateGroceryIngredientChecked(Grocery grocery, GroceryIngredient groceryIngredient) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();

                groceryDao.updateGroceryIngredientChecked(new GroceryIngredientBridge(grocery.getId(), groceryIngredient.getId(), groceryIngredient.getIsChecked()));
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void searchGroceries(String grocerySearch, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        Callable<List<Grocery>> task = () -> {
            try {
                groceryTableLock.lock();

                List<GroceryEntity> groceryEntities = groceryDao.searchGroceries("%" + grocerySearch + "%");
                List<Grocery> groceries = new ArrayList<>();
                for (GroceryEntity groceryEntity : groceryEntities) groceries.add(new Grocery(groceryEntity));
                return groceries;
            } finally {
                groceryTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Grocery>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchGroceryIngredients(long groceryId, String ingredientSearch, DbCallback<GroceryIngredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<GroceryIngredient>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();

                // get the ingredients directly added to the grocery list
                List<GroceryIngredientsTuple> tupleGrocery = groceryDao.searchGroceryIngredients(groceryId, "%"+ingredientSearch+"%");
                // get the recipe ingredients added to the grocery list
                List<RecipeIngredientInGroceryTuple> tupleRecipe = groceryDao.searchRecipeIngredientsInGrocery(groceryId, "%"+ingredientSearch+"%");
                // combine the two lists to show all Grocery ingredients
                return combineGroceryAndRecipeIngredients(tupleGrocery, tupleRecipe);
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryIngredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchRecipes(String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            try {
                recipeTableLock.lock();

                List<RecipeEntity> recipeEntities = recipeDao.searchRecipes("%" + recipeSearch + "%");
                List<OfflineRecipe> offlineRecipes = new ArrayList<>();
                for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
                return offlineRecipes;
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchRecipesInCategory(long categoryId, String recipeSearch, DbCallback<OfflineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<List<OfflineRecipe>> task = () -> {
            try {
                recipeTableLock.lock();

                List<RecipeEntity> recipeEntities = recipeDao.searchRecipesInCategory(categoryId, "%" + recipeSearch + "%");
                List<OfflineRecipe> offlineRecipes = new ArrayList<>();
                for (RecipeEntity recipeEntity : recipeEntities) offlineRecipes.add(new OfflineRecipe(recipeEntity));
                return offlineRecipes;
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<OfflineRecipe>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchRecipeCategories(String categorySearch, DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        Callable<List<RecipeCategory>> task = () -> {
            try {
                recipeTableLock.lock();

                List<RecipeCategoryEntity> recipeCategoryEntities = recipeDao.searchRecipeCategories("%" + categorySearch + "%");
                List<RecipeCategory> recipeCategories = new ArrayList<>();
                for (RecipeCategoryEntity recipeCategoryEntity : recipeCategoryEntities) recipeCategories.add(new RecipeCategory(recipeCategoryEntity));
                return recipeCategories;
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeCategory>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchRecipeIngredients(long recipeId, String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();

                List<RecipeIngredientsTuple> recipeIngredientsTuples = recipeDao.searchRecipeIngredients(recipeId,"%" + ingredientSearch + "%");
                List<Ingredient> ingredients = new ArrayList<>();
                for (RecipeIngredientsTuple tuple : recipeIngredientsTuples) ingredients.add(new Ingredient(tuple.ingredientId, tuple.onlineRecipeId,
                        tuple.ingredientName, tuple.quantity, tuple.quantityMeasId, new FoodType(tuple.foodTypeId), tuple.dateUpdated, tuple.dateSynchronized));
                return ingredients;
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }

    @Override
    public void searchIngredients(String ingredientSearch, DbCallback<Ingredient> callback) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                ingredientTableLock.lock();

                List<IngredientEntity> ingredientEntities = ingredientDao.searchIngredients("%" + ingredientSearch + "%");
                List<Ingredient> ingredients = new ArrayList<>();
                for (IngredientEntity entity : ingredientEntities) {
                    ingredients.add(new Ingredient.IngredientBuilder(entity.getIngredientId(), entity.getOnlineIngredientId(), entity.getIngredientName())
                            .setFoodType(entity.getFoodTypeId())
                            .build());
                }
                return ingredients;
            } finally {
                ingredientTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Ingredient>> futureTask = executorService.submit(task);
        callback.onResponse(futureTask.get());
    }
}
