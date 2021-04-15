package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.database.dao.GroceryDao;
import com.habbybolan.groceryplanner.database.dao.IngredientDao;
import com.habbybolan.groceryplanner.database.dao.RecipeDao;
import com.habbybolan.groceryplanner.database.dao.SectionDao;
import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeIngredientEntity;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.models.databasetuples.GroceryIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeGroceriesTuple;
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

    private static final String TAG = "DatabaseAccess";

    private Lock groceryTableLock = new ReentrantLock();
    private Lock ingredientTableLock = new ReentrantLock();
    private Lock recipeTableLock = new ReentrantLock();
    private Lock recipeCategoryLock = new ReentrantLock();

    public DatabaseAccessImpl(GroceryDao groceryDao, RecipeDao recipeDao, IngredientDao ingredientDao, SectionDao sectionDao) {
        this.groceryDao = groceryDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.sectionDao = sectionDao;
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

                // delete the grocery from the grocery table
                groceryDao.deleteGroceries(groceryIds);
                // delete the rows including the grocery from the GroceryIngredientBridge table
                groceryDao.deleteGroceriesFromBridge(groceryIds);
                // delete grocery from GroceryRecipeBridge
                groceryDao.deleteGroceriesFromGroceryRecipeBridge(groceryIds);
                // delete Grocery from GroceryIngredientEntity table
                groceryDao.deleteGroceriesFromGroceryIngredientEntity(groceryIds);
                // delete Grocery from GroceryRecipeIngredientEntity table
                groceryDao.deleteGroceriesFromGroceryRecipeIngredientEntity(groceryIds);

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
                List<RecipeGroceriesTuple> groceries = recipeDao.getGroceriesHoldingRecipe(recipe.getId());
                List<GroceryRecipe> groceryRecipes = new ArrayList<>();
                for (RecipeGroceriesTuple grocery : groceries) {
                    groceryRecipes.add(new GroceryRecipe( grocery.groceryName, grocery.groceryId, grocery.amount));
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
                        // todo: could do this better
                        boolean isGroceryHasIngredient = groceryDao.getCountIngredientInGroceryBridge(grocery.getId(), ingredient.getId()) >= 1;
                        // if the grocery doesn't already contain the ingredient, add it to groceryIngredientBridge
                        if (!isGroceryHasIngredient) {
                            GroceryIngredientBridge bridge = new GroceryIngredientBridge(grocery.getId(), ingredient.getId(), false);
                            groceryDao.insertIngredientIntoGrocery(bridge);
                        }
                    } else {
                        groceryDao.deleteRecipeIngredientFromGrocery(ingredient.getId(), recipe.getId(), grocery.getId());
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
                // delete Recipe from GroceryRecipeBridge
                recipeDao.deleteRecipesFromGroceryRecipeBridge(recipeIds);
                // delete Recipe from RecipeIngredientBridge
                recipeDao.deleteRecipesFromRecipeIngredientBridge(recipeIds);
                // delete recipe rows from GroceryRecipeIngredientEntity
                recipeDao.deleteRecipeFromGroceryRecipeIngredient(recipeIds);
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

    @Override
    public void addIngredient(Ingredient ingredient) {
        Runnable task = () -> {
            try {
                ingredientTableLock.lock();
                ingredientDao.insertIngredient(new IngredientEntity(ingredient));
            } finally {
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
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
                    // delete the ingredient from RecipeIngredientBridge
                    ingredientDao.deleteIngredientsFromRecipeIngredientBridge(ingredientIds);
                    // delete the ingredient from GroceryRecipeIngredientEntity
                    ingredientDao.deleteIngredientsFromGroceryRecipeIngredientEntity(ingredientIds);
                    // delete the ingredient from GroceryIngredientEntity
                    ingredientDao.deleteIngredientsFromGroceryIngredientEntity(ingredientIds);
                    // delete the ingredient from GroceryIngredientBridge
                    ingredientDao.deleteIngredientsFromGroceryIngredientBridge(ingredientIds);

            } finally {
                ingredientTableLock.unlock();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void deleteIngredientsFromHolder(IngredientHolder ingredientHolder, List<Long> ingredientIds) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            deleteIngredientsFromGrocery(ingredientHolder.getId(), ingredientIds);
        } else {
            // Otherwise, delete the Ingredient relationship with the recipe
            deleteIngredientsFromRecipe(ingredientHolder.getId(), ingredientIds);
        }
    }

    @Override
    public void deleteIngredientFromHolder(IngredientHolder ingredientHolder, long ingredientId) {
        if (ingredientHolder.isGrocery()) {
            // If the ingredientHolder is a Grocery, then delete the Ingredient relationship from Grocery
            deleteIngredientsFromGrocery(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredientId);}});
        } else {
            // Otherwise, delete the Ingredient relationship with the recipe
            deleteIngredientsFromRecipe(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredientId);}});
        }
    }

    /**
     * Delete a list of ingredients from the grocery list
     * @param groceryId       The grocery id to delete the ingredients from
     * @param ingredientIds   The list of ingredient ids to delete from the grocery
     */
    private void deleteIngredientsFromGrocery(long groceryId, List<Long> ingredientIds) {
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

    /**
     * Delete a list of ingredients from the recipe list
     * @param recipeId        The recipe id to delete the ingredients from
     * @param ingredientIds   The list of ingredient ids to delete from the recipe
     */
    private void deleteIngredientsFromRecipe(long recipeId, List<Long> ingredientIds) {
        Runnable task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();

                recipeDao.deleteIngredientsFromRecipeIngredientBridge(recipeId, ingredientIds);
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
    public void fetchIngredients(ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException {
        Callable<List<Ingredient>> task = () -> {
            try {
                recipeTableLock.lock();
                ingredientTableLock.lock();
                List<Ingredient> ingredients = new ArrayList<>();

                List<IngredientEntity> ingredientEntities = ingredientDao.getIngredients();
                for (int i = 0; i < ingredientEntities.size(); i++) {
                    IngredientEntity ingredientEntity = ingredientEntities.get(i);
                    //Section section = new Section(recipeIngredientBridge.sectionEntity);
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
                // retrieve the Grocery ingredients added directly
                for (GroceryIngredientsTuple tuple : tupleGrocery) {
                    Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName,
                            tuple.price, tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
                    GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked, true);
                    map.put(tuple.ingredientId, groceryIngredient);
                }
                // retrieve the recipe ingredients added to the grocery
                for (RecipeIngredientInGroceryTuple tuple : tupleRecipe) {
                    if (!map.containsKey(tuple.ingredientId)) {
                        Ingredient ingredient = new Ingredient(tuple.ingredientId, tuple.ingredientName,
                                tuple.price, tuple.pricePer, tuple.priceType, tuple.quantity, tuple.quantityType, new FoodType(tuple.foodType));
                        GroceryIngredient groceryIngredient = new GroceryIngredient(ingredient, tuple.isChecked, false);
                        map.put(tuple.ingredientId, groceryIngredient);
                    }
                    GroceryIngredient groceryIngredient = map.get(tuple.ingredientId);
                    RecipeWithIngredient recipeWithIngredient = new RecipeWithIngredient(tuple.recipeId, tuple.recipeName, tuple.amount, tuple.quantity, tuple.quantityType);
                    groceryIngredient.addRecipe(recipeWithIngredient);
                }

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
}
