package com.habbybolan.groceryplanner.database;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.dao.SectionDao;
import com.habbybolan.groceryplanner.database.dao.StepsDao;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.StepsEntity;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.databasetuples.GroceryIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientInGroceryTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsWithGroceryTuple;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.ingredientmodels.RecipeWithIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.primarymodels.Step;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.HashMap;
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
    private SectionDao sectionDao;
    private StepsDao stepsDao;


    private static final String TAG = "DatabaseAccess";

    private Lock groceryTableLock = new ReentrantLock();
    private Lock ingredientTableLock = new ReentrantLock();
    private Lock recipeTableLock = new ReentrantLock();
    private Lock sectionTableLock = new ReentrantLock();
    private Lock stepsTableLock = new ReentrantLock();
    private Lock recipeCategoryLock = new ReentrantLock();

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
        deleteGroceries(new ArrayList<Grocery>(){{add(grocery);}});
    }

    @Override
    public void deleteGroceries(List<Grocery> groceries) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Grocery grocery : groceries) {
                    GroceryEntity groceryEntity = new GroceryEntity(grocery);
                    // delete the grocery from the grocery table
                    groceryDao.deleteGrocery(groceryEntity);
                    // delete the rows including the grocery from the GroceryIngredientBridge table
                    groceryDao.deleteGroceryFromBridge(groceryEntity.getGroceryId());
                    // delete grocery from GroceryRecipeBridge
                    groceryDao.deleteGroceryFromGroceryRecipeBridge(grocery.getId());
                    // delete Grocery from GroceryIngredientEntity table
                    groceryDao.deleteGroceryFromGroceryIngredientEntity(grocery.getId());
                    // delete Grocery from GroceryRecipeIngredientEntity table
                    groceryDao.deleteGroceryFromGroceryRecipeIngredientEntity(grocery.getId());
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
    public void fetchGroceries(ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException {
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
        groceriesObserver.clear();
        groceriesObserver.addAll(groceries);
    }

    @Override
    public void fetchGroceriesHoldingRecipe(Recipe recipe, ObservableArrayList<GroceryRecipe> groceriesObserver) throws ExecutionException, InterruptedException {
        Callable<List<GroceryRecipe>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                // todo: make into one query
                List<GroceryEntity> groceries = recipeDao.getGroceriesHoldingRecipe(recipe.getId()).groceries;
                List<GroceryRecipeBridge> groceryRecipeBridges = recipeDao.getRecipeAmountsInsideGrocery(recipe.getId());
                List<GroceryRecipe> groceryRecipes = new ArrayList<>();
                for (int i = 0; i < groceries.size(); i++) {
                    GroceryEntity groceryEntity = groceries.get(i);
                    GroceryRecipeBridge groceryRecipeBridge = groceryRecipeBridges.get(i);
                    groceryRecipes.add(new GroceryRecipe(groceryEntity.getName(), groceryEntity.getGroceryId(), groceryEntity.getIsFavorite(), groceryRecipeBridge.amount));
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
        groceriesObserver.clear();
        groceriesObserver.addAll(groceryRecipes);
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(Recipe recipe, ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException {
        Callable<List<GroceryEntity>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                return recipeDao.getGroceriesNotHoldingRecipe(recipe.getId());
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
        groceriesObserver.clear();
        groceriesObserver.addAll(groceries);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> recipeIngredients) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();

                // add the recipe to the GroceryRecipeBridge if it doesn't already exist, or update with new amount
                GroceryRecipeBridge groceryRecipeBridge = new GroceryRecipeBridge(grocery.getId(), recipe.getId(), amount);
                recipeDao.insertRecipeIntoGrocery(groceryRecipeBridge);

                // insert or remove from GroceryRecipeIngredientEntity and GroceryIngredientBridge
                for (IngredientWithGroceryCheck ingredient : recipeIngredients) {
                    GroceryRecipeIngredientEntity entity = new GroceryRecipeIngredientEntity(grocery.getId(),
                            ingredient.getId(), recipe.getId(), ingredient.getQuantity(), ingredient.getQuantityType());

                    // if the ingredient is checked as add to grocery, then add, otherwise delete from grocery
                    if (ingredient.getIsInGrocery()) {
                        groceryDao.insertGroceryRecipeIngredient(entity);
                        boolean isGroceryHasIngredient = groceryDao.getCountIngredientInGroceryBridge(grocery.getId(), ingredient.getId()) >= 1;
                        // if the grocery doesn't already contain the ingredient, add it to groceryIngredientBridge
                        if (!isGroceryHasIngredient) {
                            GroceryIngredientBridge bridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                            groceryDao.insertIngredientIntoGrocery(bridge);
                        }
                    } else {
                        groceryDao.deleteRecipeIngredientFromGrocery(ingredient.getId(), recipe.getId(), grocery.getId());
                        groceryDao.deleteIngredientsNotInGroceryAnymore();
                    }

                    GroceryIngredientBridge bridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                    groceryDao.insertIngredientIntoGrocery(bridge);
                }
            } finally {
                groceryTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteGroceryRecipeBridge(Recipe recipe, Grocery grocery) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), recipe.getId(), 0);
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
    public void deleteRecipe(Recipe recipe) {
        deleteRecipes(new ArrayList<Recipe>(){{add(recipe);}});
    }

    @Override
    public void deleteRecipes(List<Recipe> recipes) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                for (Recipe recipe : recipes) {
                    RecipeEntity recipeEntity = new RecipeEntity(recipe);
                    // delete the recipe from the RecipeEntity table
                    recipeDao.deleteRecipe(recipeEntity);
                    // delete Recipe from GroceryRecipeBridge
                    recipeDao.deleteRecipeFromGroceryRecipeBridge(recipeEntity.getRecipeId());
                    // delete Recipe from RecipeIngredientBridge
                    recipeDao.deleteRecipeFromRecipeIngredientBridge(recipe.getId());
                    // delete recipe rows from GroceryRecipeIngredientEntity
                    recipeDao.deleteRecipeFromGroceryRecipeIngredient(recipe.getId());
                    groceryDao.deleteIngredientsNotInGroceryAnymore();
                }
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
    public void fetchRecipes(Long recipeCategoryId, ObservableArrayList<Recipe> recipesObserver) throws ExecutionException, InterruptedException {
        Callable<List<RecipeEntity>> task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();
                if (recipeCategoryId == null)
                    return recipeDao.getAllRecipes();
                else
                    // todo: should this return all or only un-categorized recipes
                    return recipeDao.getAllRecipes(recipeCategoryId);
            } finally {
                recipeCategoryLock.unlock();
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
        recipesObserver.clear();
        recipesObserver.addAll(recipes);

    }

    @Override
    public List<Recipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException {
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
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeEntity> recipeEntities = futureTask.get();
        for (RecipeEntity recipeEntity : recipeEntities) {
            recipes.add(new Recipe(recipeEntity));
        }
        return recipes;
    }

    @Override
    public void updateRecipes(ArrayList<Recipe> recipes) {
        Runnable task = () -> {
            recipeTableLock.lock();
            try {
                for (Recipe recipe : recipes) {
                    recipeDao.updateRecipes(new RecipeEntity(recipe));
                }
            } finally {
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipe(ObservableField<Recipe> recipeObserver, long recipeId) throws ExecutionException, InterruptedException {
        Callable<RecipeEntity> task = () -> {
            try {
                recipeTableLock.lock();
                return recipeDao.getRecipe(recipeId);
            } finally {
                recipeTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<RecipeEntity> futureTask = executorService.submit(task);
        Recipe recipe = new Recipe(futureTask.get());
        recipeObserver.set(recipe);
    }

    // Recipe Category

    @Override
    public void deleteRecipeCategory(RecipeCategory recipeCategory) {
        deleteRecipeCategories(new ArrayList<RecipeCategory>(){{add(recipeCategory);}});
    }

    @Override
    public void deleteRecipeCategories(List<RecipeCategory> recipeCategories) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();
                for (RecipeCategory recipeCategory : recipeCategories) {
                    RecipeCategoryEntity recipeCategoryEntity = new RecipeCategoryEntity(recipeCategory);
                    // delete the grocery from the grocery table
                    recipeDao.deleteRecipeCategory(recipeCategoryEntity);
                }
            } finally {
                recipeCategoryLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void fetchRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException {
        Callable<List<RecipeCategoryEntity>> task = () -> {
            try {
                recipeTableLock.lock();
                recipeCategoryLock.lock();
                return recipeDao.getAllRecipeCategories();
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
        recipeCategoriesObserved.clear();
        recipeCategoriesObserved.addAll(recipeCategories);
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


    // Steps

    @Override
    public List<Step> fetchStepsFromRecipe(long recipeId) throws ExecutionException, InterruptedException {
        // execute database access with Callable
        Callable<List<StepsEntity>> task = () -> {
            stepsTableLock.lock();
            try {
                return stepsDao.getStepByRecipe(recipeId);
            } finally {
                stepsTableLock.unlock();
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<StepsEntity>> futureTask = executorService.submit(task);
        List<Step> steps = new ArrayList<>();
        List<StepsEntity> stepEntities = futureTask.get();
        for (StepsEntity stepsEntity : stepEntities) {
            steps.add(new Step(stepsEntity));
        }
        Log.i(TAG, "fetchStepsFromRecipe: " + steps);
        return steps;
    }

    @Override
    public void addStep(Step step) {
        Runnable task = () -> {
            stepsTableLock.lock();
            try {
                stepsDao.insertStep(new StepsEntity(step));
            } finally {
                stepsTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    // Section
    // todo:


    // INGREDIENTS

    @Override
    public void addIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then associate the Ingredient to the grocery
            insertIngredientsIntoGrocery((Grocery) ingredientHolder, ingredients);
        } else {
            // Otherwise, associated the Ingredient with the recipe
            insertIngredientsIntoRecipe((Recipe) ingredientHolder, ingredients);
        }
    }

    @Override
    public void addIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then associate the Ingredient to the grocery
            insertIngredientsIntoGrocery((Grocery) ingredientHolder, new ArrayList<Ingredient>(){{add(ingredient);}});
        } else {
            // Otherwise, associated the Ingredient with the recipe
            insertIngredientsIntoRecipe((Recipe) ingredientHolder, new ArrayList<Ingredient>(){{add(ingredient);}});
        }
    }

    /**
     * Insert an ingredient into a grocery list
     * @param grocery        Grocery list to insert the ingredient into
     * @param ingredients    Ingredients to insert
     */
    private void insertIngredientsIntoGrocery(Grocery grocery, List<Ingredient> ingredients) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    long id = ingredientDao.insertIngredient(new IngredientEntity(ingredient));
                    ingredient.setId(id);
                    // insert into bridge
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                    groceryDao.insertIntoBridge(groceryIngredientBridge);
                    // insert into GroceryIngredientEntity
                    GroceryIngredientEntity groceryIngredientEntity = new GroceryIngredientEntity(grocery.getId(), ingredient.getId(), ingredient.getQuantity(), ingredient.getQuantityType());
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

    /**
     * Insert an ingredient into a recipe list
     * @param recipe         Recipe list to insert the ingredient into
     * @param ingredients    Ingredients to insert
     */
    private void insertIngredientsIntoRecipe(Recipe recipe, List<Ingredient> ingredients) {
        RecipeEntity recipeEntity = new RecipeEntity(recipe);
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    long id = ingredientDao.insertIngredient(new IngredientEntity(ingredient));
                    ingredient.setId(id);
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.insertIntoBridge(recipeIngredientBridge);
                }
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            deleteIngredientsFromGrocery((Grocery) ingredientHolder, ingredients);
        } else {
            // Otherwise, delete the Ingredient relationship with the recipe
            deleteIngredientsFromRecipe((Recipe)ingredientHolder, ingredients);
        }
    }

    @Override
    public void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            deleteIngredientsFromGrocery((Grocery) ingredientHolder, new ArrayList<Ingredient>(){{add(ingredient);}});
        } else {
            // Otherwise, delete the Ingredient relationship with the recipe
            deleteIngredientsFromRecipe((Recipe)ingredientHolder, new ArrayList<Ingredient>(){{add(ingredient);}});
        }
    }

    /**
     * Delete a list of ingredients from the grocery list
     * @param grocery       The Grocery list to delete ingredients from
     * @param ingredients   The list of ingredients to delete
     */
    private void deleteIngredientsFromGrocery(Grocery grocery, List<Ingredient> ingredients) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    // create dummy GroceryIngredientEntity to delete from table
                    GroceryIngredientEntity entity = new GroceryIngredientEntity(grocery.getId(), ingredient.getId(), 0, "");
                    groceryDao.deleteIngredientFromGroceryIngredient(entity);
                    // delete from GroceryIngredientBridge if the Grocery does not contain the Ingredient from a recipe or the grocery list
                    groceryDao.deleteIngredientsNotInGroceryAnymore();
                }
            } finally {
                ingredientTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Delete a list of ingredients from the recipe list
     * @param recipe        The recipe list to delete ingredients from
     * @param ingredients   The list of ingredients to delete
     */
    private void deleteIngredientsFromRecipe(Recipe recipe, List<Ingredient> ingredients) {
        RecipeEntity recipeEntity = new RecipeEntity(recipe);
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.deleteFromRecipeIngredientBridge(recipeIngredientBridge);
                    recipeDao.deleteRecipeIngredientFromGroceryRecipeIngredient(recipe.getId(), ingredient.getId());
                    // delete from GroceryIngredientBridge if the Grocery does not contain the Ingredient from a recipe or the grocery list
                    groceryDao.deleteIngredientsNotInGroceryAnymore();
                }
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredientHolderRelationship(IngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            Grocery grocery = (Grocery) ingredientHolder;
            Runnable task = () -> {
                try {
                    groceryTableLock.lock();
                    ingredientTableLock.lock();
                    /*
                    // create dummy GroceryIngredientEntity to delete
                    GroceryIngredientEntity groceryIngredientEntity = new GroceryIngredientEntity(grocery.getId(), ingredient.getId(), 0, "");
                    groceryDao.deleteFromGroceryIngredientEntity(groceryIngredientEntity);
                    // todo: check if the ingredient should be deleted from bridge before deleting (not inside GroceryIngredientEntity or GroceryIngredientRecipeEntity)
                    if (false) {
                        GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                        groceryDao.deleteFromGroceryIngredientBridge(groceryIngredientBridge);
                    }*/
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
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.deleteFromRecipeIngredientBridge(recipeIngredientBridge);
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
     * Get all the Ingredients associated with a given Recipe object.
     * @param recipe   The Recipe object holding Ingredients
     * @param ingredientsObserver
     */
    @Override
    public void fetchIngredientsFromRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                // todo: do in one query
                List<IngredientEntity> ingredientEntities = recipeDao.getIngredientsFromRecipe(recipe.getId()).ingredients;
                List<RecipeIngredientBridge> recipeIngredientBridges = recipeDao.getRecipeIngredient(recipe.getId());
                for (int i = 0; i < recipeIngredientBridges.size(); i++) {
                    RecipeIngredientBridge recipeIngredientBridge = recipeIngredientBridges.get(i);
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    //Section section = new Section(recipeIngredientBridge.sectionEntity);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getIngredientName())
                            .setPrice(ingredientEntity.getPrice())
                            .setPriceType(ingredientEntity.getPriceType())
                            .setPricePer(ingredientEntity.getPricePer())
                            .setQuantity(recipeIngredientBridge.quantity)
                            .setQuantityType(recipeIngredientBridge.quantityType)
                            //.setSection(section)
                            .setFoodType(ingredientEntity.getFoodType())
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
        ingredientsObserver.clear();
        ingredientsObserver.addAll(futureTask.get());
    }

    @Override
    public void deleteRecipeFromGrocery(Grocery grocery, Recipe recipe) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                // delete recipe and grocery association in GroceryRecipeBridge
                GroceryRecipeBridge bridge = new GroceryRecipeBridge(grocery.getId(), recipe.getId(), 0);
                groceryDao.deleteGroceryRecipe(bridge);
                // delete ingredients associated with recipe and grocery added to GroceryRecipeIngredientEntity
                recipeDao.deleteRecipeGroceryFromGroceryRecipeIngredient(grocery.getId(), recipe.getId());
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
    public void fetchRecipeIngredientsInGrocery(Grocery grocery, Recipe recipe, ObservableArrayList<IngredientWithGroceryCheck> ingredientObserver) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
                List<RecipeIngredientsWithGroceryTuple> tuples = recipeDao.getRecipeIngredientsInGrocery(recipe.getId(), grocery.getId());
                for (RecipeIngredientsWithGroceryTuple tuple : tuples) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName, tuple.price,
                            tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
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
        ingredientObserver.clear();
        ingredientObserver.addAll(futureTask.get());
    }

    @Override
    public void fetchRecipeIngredientsNotInGrocery(Recipe recipe, ObservableArrayList<IngredientWithGroceryCheck> ingredientObserver) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithGroceryCheck>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<IngredientWithGroceryCheck> ingredients = new ArrayList<>();
                List<RecipeIngredientsTuple> tuples = recipeDao.getRecipeIngredients(recipe.getId());
                for (RecipeIngredientsTuple tuple : tuples) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName, tuple.price,
                            tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
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
        ingredientObserver.clear();
        ingredientObserver.addAll(futureTask.get());
    }

    /*
    @Override
    public void fetchIngredientsFromRecipeWithCheck(Recipe recipe, Grocery grocery, ObservableArrayList<IngredientWithCheck> ingredientObserver) throws ExecutionException, InterruptedException {
        Callable<List<IngredientWithCheck>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<IngredientWithCheck> ingredients = new ArrayList<>();
                List<GroceryRecipeIngredientTuple> tuples = recipeDao.getIngredientsInRecipeWithCheck(recipe.getId(), grocery.getId());
                for (GroceryRecipeIngredientTuple tuple : tuples) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName, tuple.price,
                            tuple.pricePer, tuple.priceType, tuple.recipeQuantity, tuple.recipeQuantityType, new FoodType(tuple.foodType));
                    IngredientWithCheck ingredientWithCheck = new IngredientWithCheck(ingredient, tuple.isChecked);
                    ingredients.add(ingredientWithCheck);
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
        Future<List<IngredientWithCheck>> futureTask = executorService.submit(task);
        ingredientObserver.clear();
        ingredientObserver.addAll(futureTask.get());
    }*/

    @Override
    public void fetchIngredientsNotInRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = recipeDao.getIngredientsNotInRecipe(recipe.getId());
                for (int i = 0; i < ingredientEntities.size(); i++) {
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    //Section section = new Section(recipeIngredientBridge.sectionEntity);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getIngredientName())
                            .setPrice(ingredientEntity.getPrice())
                            .setPriceType(ingredientEntity.getPriceType())
                            .setPricePer(ingredientEntity.getPricePer())
                            //.setSection(section)
                            .setFoodType(ingredientEntity.getFoodType())
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
        ingredientsObserver.clear();
        ingredientsObserver.addAll(futureTask.get());
    }

    @Override
    public void fetchIngredientsNotInGrocery(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();
                List<IngredientEntity> ingredientEntities = groceryDao.getIngredientsNotInGrocery(grocery.getId());
                for (int i = 0; i < ingredientEntities.size(); i++) {
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    ingredients.add(new Ingredient.IngredientBuilder(ingredientEntity.getIngredientId(), ingredientEntity.getIngredientName())
                            .setPrice(ingredientEntity.getPrice())
                            .setPriceType(ingredientEntity.getPriceType())
                            .setPricePer(ingredientEntity.getPricePer())
                            .setFoodType(ingredientEntity.getFoodType())
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
        ingredientsObserver.clear();
        ingredientsObserver.addAll(futureTask.get());
    }

    @Override
    public void fetchGroceryIngredients(Grocery grocery, ObservableArrayList<GroceryIngredient> groceryIngredientsObserver) throws ExecutionException, InterruptedException {
        Callable<List<GroceryIngredient>> task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();

                List<GroceryIngredientsTuple> tupleGrocery = groceryDao.getGroceryIngredients(grocery.getId());
                List<RecipeIngredientInGroceryTuple> tupleRecipe = groceryDao.getRecipeIngredientsInGrocery(grocery.getId());
                Map<Long, GroceryIngredient> map = new HashMap<>();
                for (GroceryIngredientsTuple tuple : tupleGrocery) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName,
                            tuple.price, tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
                    GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked);
                    map.put(tuple.ingredientId, groceryIngredient);
                }
                for (RecipeIngredientInGroceryTuple tuple : tupleRecipe) {
                    if (!map.containsKey(tuple.ingredientId)) {
                        Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName,
                                tuple.price, tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
                        GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked);
                        map.put(tuple.ingredientId, groceryIngredient);
                    }
                    GroceryIngredient groceryIngredient = map.get(tuple.ingredientId);
                    RecipeWithIngredient recipeWithIngredient = new RecipeWithIngredient(tuple.recipeId, tuple.recipeName, tuple.amount, tuple.quantity, tuple.quantityType);
                    groceryIngredient.addRecipe(recipeWithIngredient);
                }
                /*List<GroceryRecipeIngredientTuple> tupleList = groceryDao.getRecipeGroceryIngredients(grocery.getId());
                HashMap<Long, GroceryIngredient> map = new HashMap<>();
                for (GroceryRecipeIngredientTuple tuple : tupleList) {
                    if (!map.containsKey(tuple.ingredientId)) {
                        Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName, tuple.price,
                                tuple.pricePer, tuple.priceType, tuple.groceryQuantity, tuple.groceryQuantityType, new FoodType(tuple.foodType));
                        GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked);
                        map.put(tuple.ingredientId, groceryIngredient);
                    }
                    RecipeWithIngredient recipeWithIngredient = new RecipeWithIngredient(tuple.recipeId, tuple.recipeName, tuple.amount, tuple.recipeQuantity, tuple.recipeQuantityType);
                    map.get(tuple.ingredientId).addRecipe(recipeWithIngredient);
                }*/
                List<GroceryIngredient> groceryIngredients = new ArrayList<>();
                groceryIngredients.addAll(map.values());
                return groceryIngredients;
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        // execute database access with Callable
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<GroceryIngredient>> futureTask = executorService.submit(task);
        groceryIngredientsObserver.clear();
        groceryIngredientsObserver.addAll(futureTask.get());
    }

    @Override
    public void updateGroceryIngredientChecked(Grocery grocery, GroceryIngredient groceryIngredient) {
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                recipeTableLock.lock();
                ingredientTableLock.lock();
                // todo: updating if the ingredient is checked off or not
                // update GroceryIngredientBridge with new isChecked value
                /*GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(grocery.getId(), groceryIngredient.getId(),
                        groceryIngredient.getIsFavorite(), groceryIngredient.getQuantity(), groceryIngredient.getQuantityType(), groceryIngredient.getIsChecked());
                groceryDao.updateGroceryIngredients(groceryIngredientBridge);
                // update GroceryRecipeIngredientBridge with new isChecked value
                for (RecipeWithIngredient recipeWithIngredient : groceryIngredient.getRecipeWithIngredients()) {
                    GroceryRecipeIngredientBridge groceryRecipeIngredientBridge = new GroceryRecipeIngredientBridge(grocery.getId(), recipeWithIngredient.getRecipeId(),
                            groceryIngredient.getId(), groceryIngredient.getIsChecked(), groceryIngredient.getIsFavorite());
                    groceryDao.updateGroceryRecipeIngredientBridge(groceryRecipeIngredientBridge);
                }*/
            } finally {
                ingredientTableLock.unlock();
                recipeTableLock.unlock();
                groceryTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
