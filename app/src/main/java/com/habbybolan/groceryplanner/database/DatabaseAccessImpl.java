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
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.StepsEntity;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;
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
                    // delete the rows including the grocery from the bridge table
                    groceryDao.deleteGroceryFromBridge(groceryEntity.getGroceryId());
                    // delete the grocery from the grocery table
                    groceryDao.deleteGrocery(groceryEntity);
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
                    // delete the rows including the grocery from the bridge table
                    recipeDao.deleteRecipeFromBridge(recipeEntity.getRecipeId());
                    // delete the grocery from the grocery table
                    recipeDao.deleteRecipe(recipeEntity);
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
        GroceryEntity groceryEntity = new GroceryEntity(grocery);
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    ingredientDao.insertIngredient(new IngredientEntity(ingredient));
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryEntity, ingredient);
                    groceryDao.insertIntoBridge(groceryIngredientBridge);
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
                    ingredientDao.insertIngredient(new IngredientEntity(ingredient));
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
        GroceryEntity groceryEntity = new GroceryEntity(grocery);
        Runnable task = () -> {
            try {
                groceryTableLock.lock();
                ingredientTableLock.lock();
                for (Ingredient ingredient : ingredients) {
                    ingredientDao.deleteIngredient(new IngredientEntity(ingredient));
                    GroceryIngredientBridge groceryIngredientBridge = new GroceryIngredientBridge(groceryEntity, ingredient);
                    groceryDao.deleteFromBridge(groceryIngredientBridge);
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
                    ingredientDao.deleteIngredient(new IngredientEntity(ingredient));
                    RecipeIngredientBridge recipeIngredientBridge = new RecipeIngredientBridge(recipeEntity, ingredient);
                    recipeDao.deleteFromBridge(recipeIngredientBridge);
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
            GroceryEntity groceryEntity = new GroceryEntity(grocery);
            Runnable task = () -> {
                try {
                    groceryTableLock.lock();
                    ingredientTableLock.lock();
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
    public void fetchIngredientsFromGrocery(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
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
}
