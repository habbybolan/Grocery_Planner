package com.habbybolan.groceryplanner.database;

import android.util.Log;

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
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

import java.util.ArrayList;
import java.util.List;
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
    private SectionDao sectionDao;
    private StepsDao stepsDao;

    private static final String TAG = "DatabaseAccess";

    private Lock groceryTableLock = new ReentrantLock();
    private Lock ingredientTableLock = new ReentrantLock();
    private Lock recipeTableLock = new ReentrantLock();
    private Lock sectionTableLock = new ReentrantLock();
    private Lock stepsTableLock = new ReentrantLock();

    public DatabaseAccessImpl(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao, SectionDao sectionDao, StepsDao stepsDao) {
        this.groceryDao = groceryDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.sectionDao = sectionDao;
        this.stepsDao = stepsDao;
    }

    // GROCERIES

    @Override
    public void deleteGrocery(Grocery grocery) {
        GroceryEntity groceryEntity = new GroceryEntity(grocery);
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                // delete the rows including the grocery from the bridge table
                groceryDao.deleteGroceryFromBridge(groceryEntity.getGroceryId());
                // delete the grocery from the grocery table
                groceryDao.deleteGrocery(groceryEntity);
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
    public List<Grocery> fetchGroceries() throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> {
            try {
                groceryTableLock.lock();
                return groceryDao.getAllGroceries();
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
        return groceries;
    }


    // RECIPES


    @Override
    public void deleteRecipe(Recipe recipe) {
        RecipeEntity recipeEntity = new RecipeEntity(recipe);
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                // delete the rows including the grocery from the bridge table
                recipeDao.deleteRecipeFromBridge(recipeEntity.getRecipeId());
                // delete the grocery from the grocery table
                recipeDao.deleteRecipe(recipeEntity);
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void addRecipe(Recipe recipe) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeDao.insertRecipe(new RecipeEntity(recipe));
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public List<Recipe> fetchRecipes() throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            try {
                recipeTableLock.lock();
                return recipeDao.getAllRecipes();
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<RecipeEntity>> futureTask = executorService.submit(task);
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeEntity> recipeEntities = futureTask.get();
        for (RecipeEntity recipeEntity : recipeEntities) {
            recipes.add(new Recipe(recipeEntity));
        }
        return recipes;
    }

    // Steps

    @Override
    public List<Step> fetchStepsFromRecipe(long recipeId) throws ExecutionException, InterruptedException {
        // execute database access with Callable
        Callable<List<Step>> task = () -> {
            List<Step> steps = new ArrayList<>();
            List<StepsEntity> stepsEntities = stepsDao.getStepByRecipe(recipeId);
            for (StepsEntity stepsEntity : stepsEntities) {
                steps.add(new Step(stepsEntity));
            }
            return steps;
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Step>> futureTask = executorService.submit(task);
        List<Step> steps = futureTask.get();
        Log.i(TAG, "fetchStepsFromRecipe: " + steps);
        return steps;
    }

    @Override
    public void addStep(Step step) {
        Runnable task = () -> {
            stepsDao.insertStep(new StepsEntity(step));
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    // Section



    // INGREDIENTS


    @Override
    public void addIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then associate the Ingredient to the grocery
            Grocery grocery = (Grocery) ingredientHolder;
            GroceryEntity groceryEntity = new GroceryEntity(grocery);
            Runnable task = () -> {
                try {
                    groceryTableLock.lock();
                    ingredientTableLock.lock();
                    ingredientDao.insertIngredient(new IngredientEntity(ingredient));
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryEntity, ingredient);
                    groceryDao.insertIntoBridge(groceryIngredientBridge);
                } finally {
                    ingredientTableLock.unlock();
                    groceryTableLock.unlock();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        } else {
            // Otherwise, associated the Ingredient with the recipe
            Recipe recipe = (Recipe) ingredientHolder;
            RecipeEntity recipeEntity = new RecipeEntity(recipe);
            Runnable task = () -> {
                try {
                    recipeTableLock.lock();
                    ingredientTableLock.lock();
                    ingredientDao.insertIngredient(new IngredientEntity(ingredient));
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.insertIntoBridge(recipeIngredientBridge);
                } finally {
                    ingredientTableLock.unlock();
                    recipeTableLock.unlock();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    @Override
    public void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            Grocery grocery = (Grocery) ingredientHolder;
            GroceryEntity groceryEntity = new GroceryEntity(grocery);
            Runnable task = () -> {
                try {
                    groceryTableLock.lock();
                    ingredientTableLock.lock();
                    ingredientDao.deleteIngredient(new IngredientEntity(ingredient));
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryEntity, ingredient);
                    groceryDao.deleteFromBridge(groceryIngredientBridge);
                } finally {
                    ingredientTableLock.unlock();
                    groceryTableLock.unlock();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        } else {
            // Otherwise, delete the Ingredient relationship with the recipe
            Recipe recipe = (Recipe) ingredientHolder;
            RecipeEntity recipeEntity = new RecipeEntity(recipe);
            Runnable task = () -> {
                try {
                    recipeTableLock.lock();
                    ingredientTableLock.lock();
                    ingredientDao.deleteIngredient(new IngredientEntity(ingredient));
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.deleteFromBridge(recipeIngredientBridge);
                } finally {
                    ingredientTableLock.unlock();
                    recipeTableLock.unlock();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    /**
     * Get all the Ingredients associated with a given Grocery object.
     * @param grocery   The Grocery object holding Ingredients
     * @return          The Ingredients associated with 'grocery'
     */
    @Override
    public List<Ingredient> fetchIngredientsFromGrocery(Grocery grocery) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = groceryDao.getIngredientsFromGrocery(grocery.getId()).ingredients;
                List<GroceryIngredientBridge> groceryIngredientBridges = groceryDao.getGroceryIngredient(grocery.getId());
                for (int i = 0; i < groceryIngredientBridges.size(); i++) {
                    GroceryIngredientBridge groceryIngredientBridge = groceryIngredientBridges.get(i);
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientName())
                            .setPrice(ingredientEntity.getPrice())
                            .setPricePer(ingredientEntity.getPricePer())
                            .setPriceType(ingredientEntity.getPriceType())
                            .setQuantity(groceryIngredientBridge.quantity)
                            .setQuantityType(groceryIngredientBridge.quantityType)
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
        return futureTask.get();
    }

    /**
     * Get all the Ingredients associated with a given Recipe object.
     * @param recipe   The Recipe object holding Ingredients
     * @return          The Ingredients associated with 'recipe'
     */
    @Override
    public List<Ingredient> fetchIngredientsFromRecipe(Recipe recipe) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = recipeDao.getIngredientsFromRecipe(recipe.getId()).ingredients;
                List<RecipeIngredientBridge> recipeIngredientBridges = recipeDao.getRecipeIngredient(recipe.getId());
                for (int i = 0; i < recipeIngredientBridges.size(); i++) {
                    RecipeIngredientBridge recipeIngredientBridge = recipeIngredientBridges.get(i);
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    //Section section = new Section(recipeIngredientBridge.sectionEntity);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientName())
                            .setPrice(ingredientEntity.getPrice())
                            .setPriceType(ingredientEntity.getPriceType())
                            .setPricePer(ingredientEntity.getPricePer())
                            .setQuantity(recipeIngredientBridge.quantity)
                            .setQuantityType(recipeIngredientBridge.quantityType)
                            //.setSection(section)
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
        return futureTask.get();
    }
}
