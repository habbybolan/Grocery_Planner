package com.habbybolan.groceryplanner.database;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.google.gson.JsonArray;
import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.models.primarymodels.AccessLevel;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.sync.SyncRecipeFromResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseTest {

    private DatabaseAccess databaseAccess;
    private LocalDatabase db;
    private SyncRecipeFromResponse syncRecipeFromResponse;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase.class)
                .addCallback(LocalDatabase.createCallback())
                .build();
        databaseAccess = new DatabaseAccessImpl(db.getGroceryDao(), db.getRecipeDao(), db.getIngredientDao(), db.getRecipeTagDao(), db.getNutritionDao());
        // used only for accessing offline database sync with fake web service response
        syncRecipeFromResponse = new SyncRecipeFromResponse(databaseAccess);
    }

    @After
    public void tearDown() throws Exception {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: test validity of json sent to web service
    // TODO: test database retrieving sync data and storing it properly

    /**
     * Test only the recipe being inserted online, excluding ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized and onlineId
     */
    @Test
    public void OnlyMyRecipeInsertOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            // offline recipe without ingredients, nutrition, or tags not yet synced with online
            MyRecipe offlineMyRecipe = createEmptyOfflineMyRecipe(0, null);
            try {
                databaseAccess.insertMyRecipe(offlineMyRecipe, insertedRecipe -> {
                    MyRecipe createdRecipe = new MyRecipe(offlineMyRecipe, offlineMyRecipe.getAccessLevel());
                    createdRecipe.setId(insertedRecipe.getId());
                    JsonArray jsonArray = FakeSyncResponses.onlyMyRecipeInsertOnlineArray(new MyRecipe(createdRecipe, new AccessLevel(3)));
                    syncRecipeFromResponse.syncMyRecipes(jsonArray, new SyncCompleteCallback() {
                        @Override
                        public void onSyncComplete() {
                            try {
                                databaseAccess.fetchFullMyRecipe(insertedRecipe.getId(), recipeAfterSync -> {
                                    assertNotNull(recipeAfterSync.getDateSynchronized());
                                    assertNotNull(recipeAfterSync.getDateUpdated());
                                    assertEquals(FakeSyncResponses.onlineId, (long) recipeAfterSync.getOnlineId());
                                    createdRecipe.setOnlineId(recipeAfterSync.getOnlineId());
                                    // assert the only things changed were the onlineId and dateSynchronized
                                    assertThat(createdRecipe, is(recipeAfterSync));
                                });
                            } catch (InterruptedException | ExecutionException e) {
                                fail(e.getMessage());
                            }
                        }

                        @Override
                        public void onSyncFailed(String message) {
                            fail(message);
                        }
                    });
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).get();
    }

    /**
     * Test the recipe inner objects being inserted online, including its ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized and onlineId
     */
    @Test
    public void AllMyRecipeInsertOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            // offline recipe without ingredients, nutrition, or tags not yet synced with online
            MyRecipe offlineMyRecipe = createFullOfflineMyRecipe(0, null);
            try {
                databaseAccess.insertFullMyRecipe(offlineMyRecipe, insertedRecipe -> {
                    try {
                        databaseAccess.fetchFullMyRecipe(insertedRecipe.getId(), recipeBeforeSync -> {
                            JsonArray jsonArray = FakeSyncResponses.allMyRecipeInsertOnlineArray(recipeBeforeSync);
                            syncRecipeFromResponse.syncMyRecipes(jsonArray, new SyncCompleteCallback() {
                                @Override
                                public void onSyncComplete() {
                                    try {
                                        databaseAccess.fetchFullMyRecipe(insertedRecipe.getId(), recipeAfterSync -> {
                                            // Ingredients
                                            for (int i = 0; i < recipeAfterSync.getIngredients().size(); i++) {
                                                Ingredient ingredient = recipeAfterSync.getIngredients().get(0);
                                                assertNotNull(ingredient.getDateSynchronized());
                                                assertNotNull(ingredient.getDateUpdated());
                                                assertEquals(FakeSyncResponses.onlineId, (long) ingredient.getOnlineId());
                                                Ingredient defaultIngredient = defaultOfflineIngredient(ingredient.getId(), ingredient.getOnlineId());
                                                assertThat(defaultIngredient, is(ingredient));
                                            }
                                            // Tags
                                            for (int i = 0; i < recipeAfterSync.getRecipeTags().size(); i++) {
                                                RecipeTag tag = recipeAfterSync.getRecipeTags().get(i);
                                                assertNotNull(tag.getDateSynchronized());
                                                assertNotNull(tag.getDateUpdated());
                                                assertEquals(FakeSyncResponses.onlineId, (long) tag.getOnlineId());
                                                assertTrue(tag.getId() != 0);
                                                assertEquals(offlineString, tag.getTitle());
                                            }
                                            // Nutrition
                                            for (Nutrition nutrition : recipeAfterSync.getNutritionList()) {
                                                assertNotNull(nutrition.getDateUpdated());
                                                assertNotNull(nutrition.getDateSynchronized());
                                                assertEquals(offlineAmount, nutrition.getAmount());
                                                assertEquals(offlineAmount, (long) nutrition.getMeasurementType().getMeasurementId());
                                            }
                                        });
                                    } catch (InterruptedException | ExecutionException e) {
                                        fail(e.getMessage());
                                    }
                                }

                                @Override
                                public void onSyncFailed(String message) {
                                    fail(message);
                                }
                            });
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        fail();
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();
    }

    /**
     * Test only the recipe being updated online, excluding ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized
     */
    @Test
    public void OnlyMyRecipeUpdateOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createEmptyOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            // insert recipe before sync
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    myRecipe.setId(response.getId());
                    // sync recipe based on fake json response
                    syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.onlyMyRecipeUpdateOnlineArray(myRecipe), new SyncCompleteCallback() {
                        @Override
                        public void onSyncComplete() {
                            try {
                                // compare inserted synced recipe with previously created recipe
                                databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                    @Override
                                    public void onResponse(MyRecipe recipeAfterSync) {
                                        assertNotNull(recipeAfterSync.getDateUpdated());
                                        assertNotNull(recipeAfterSync.getDateSynchronized());
                                        // only the dateSynchronized should have changed
                                        assertThat(recipeAfterSync, is(myRecipe));
                                    }
                                });
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                fail();
                            }
                        }
                        @Override
                        public void onSyncFailed(String message) {
                            fail(message);
                        }
                    });
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();

    }

    /**
     * Test the recipe inner objects being updated online, including ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized
     */
    @Test
    public void AllMyRecipeUpdateOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createFullOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            // insert recipe before sync
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    // fetch full inserted recipe
                    try {
                        databaseAccess.fetchFullMyRecipe(response.getId(), new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe recipeBeforeSync) {
                                // sync recipe based on fake json response
                                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.AllMyRecipeUpdateOnlineArray(recipeBeforeSync), new SyncCompleteCallback() {
                                    @Override
                                    public void onSyncComplete() {
                                        try {
                                            // get full recipe after sync to compare with before sync
                                            databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                                @Override
                                                public void onResponse(MyRecipe recipeAfterSync) {
                                                    // ingredients
                                                    for (int i = 0; i < recipeAfterSync.getIngredients().size(); i++) {
                                                        Ingredient ingredient = recipeAfterSync.getIngredients().get(i);
                                                        assertNotNull(ingredient.getDateSynchronized());
                                                        assertNotNull(ingredient.getDateUpdated());
                                                        Ingredient ingredientBeforeSync = recipeBeforeSync.getIngredients().get(i);
                                                        ingredientBeforeSync.setOnlineId(ingredient.getOnlineId());
                                                        // assert only dateSynchronized changed
                                                        assertThat(ingredient, is(ingredientBeforeSync));
                                                    }
                                                    // tags
                                                    for (int i = 0; i < recipeAfterSync.getRecipeTags().size(); i++) {
                                                        RecipeTag tag = recipeAfterSync.getRecipeTags().get(i);
                                                        assertNotNull(tag.getDateSynchronized());
                                                        assertNotNull(tag.getDateUpdated());
                                                        RecipeTag tagBeforeSync = recipeBeforeSync.getRecipeTags().get(i);
                                                        // assert only dateSynchronized changed
                                                        assertThat(tag, is(tagBeforeSync));
                                                    }
                                                    // nutrition
                                                    for (int i = 0; i < recipeAfterSync.getNutritionList().size(); i++) {
                                                        Nutrition nutrition = recipeAfterSync.getNutritionList().get(i);
                                                        assertNotNull(nutrition.getDateSynchronized());
                                                        assertNotNull(nutrition.getDateUpdated());
                                                        Nutrition nutritionBeforeSync = recipeBeforeSync.getNutritionList().get(i);
                                                        // assert only dateSynchronized changed
                                                        assertThat(nutrition, is(nutritionBeforeSync));
                                                    }
                                                }
                                            });
                                        } catch (InterruptedException | ExecutionException e) {
                                            e.printStackTrace();
                                            fail();
                                        }
                                    }

                                    @Override
                                    public void onSyncFailed(String message) {
                                        fail(message);
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        fail();
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();
    }

    /**
     * Test only the recipe up_to_date online, excluding ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized
     */
    @Test
    public void OnlyMyRecipeUpToDateOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createEmptyOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            // insert recipe before sync
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    myRecipe.setId(response.getId());
                    // sync recipe based on fake json response
                    syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.onlyMyRecipeUpToDateArray(myRecipe), new SyncCompleteCallback() {
                        @Override
                        public void onSyncComplete() {
                            try {
                                // compare inserted synced recipe with previously created recipe
                                databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                    @Override
                                    public void onResponse(MyRecipe recipeAfterSync) {
                                        assertNotNull(recipeAfterSync.getDateUpdated());
                                        assertNotNull(recipeAfterSync.getDateSynchronized());
                                        // only the dateSynchronized should have changed
                                        assertThat(recipeAfterSync, is(myRecipe));
                                    }
                                });
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                fail();
                            }
                        }

                        @Override
                        public void onSyncFailed(String message) {
                            fail(message);
                        }
                    });
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();
    }

    /**
     * Test only the recipe inner objects up_to_date online, including ingredients, tags and nutrition.
     * The only values that will change is the dateSynchronized
     */
    @Test
    public void AllMyRecipeUpToDateOnline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createFullOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            // insert recipe before sync
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    // fetch full inserted recipe
                    try {
                        databaseAccess.fetchFullMyRecipe(response.getId(), new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe beforeSync) {
                                // sync recipe based on fake json response
                                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.AllMyRecipeUpToDateArray(beforeSync), new SyncCompleteCallback() {
                                    @Override
                                    public void onSyncComplete() {
                                        try {
                                            // get full recipe after sync to compare with before sync
                                            databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                                @Override
                                                public void onResponse(MyRecipe response) {
                                                    // ingredients
                                                    for (int i = 0; i < response.getIngredients().size(); i++) {
                                                        Ingredient ingredient = response.getIngredients().get(i);
                                                        assertNotNull(ingredient.getDateSynchronized());
                                                        assertNotNull(ingredient.getDateUpdated());
                                                        Ingredient ingredientBeforeSync = beforeSync.getIngredients().get(i);
                                                        ingredientBeforeSync.setOnlineId(ingredient.getOnlineId());
                                                        // assert only dateSynchronized changed
                                                        assertThat(ingredient, is(ingredientBeforeSync));
                                                    }
                                                    // tags
                                                    for (int i = 0; i < response.getRecipeTags().size(); i++) {
                                                        RecipeTag tag = response.getRecipeTags().get(i);
                                                        assertNotNull(tag.getDateSynchronized());
                                                        assertNotNull(tag.getDateUpdated());
                                                        RecipeTag tagBeforeSync = beforeSync.getRecipeTags().get(i);
                                                        // assert only dateSynchronized changed
                                                        assertThat(tag, is(tagBeforeSync));
                                                    }
                                                    // nutrition
                                                    for (int i = 0; i < response.getNutritionList().size(); i++) {
                                                        Nutrition nutrition = response.getNutritionList().get(i);
                                                        assertNotNull(nutrition.getDateSynchronized());
                                                        assertNotNull(nutrition.getDateUpdated());
                                                        Nutrition nutritionBeforeSync = beforeSync.getNutritionList().get(i);
                                                        // assert only dateSynchronized changed
                                                        assertThat(nutrition, is(nutritionBeforeSync));
                                                    }
                                                }
                                            });
                                        } catch (InterruptedException | ExecutionException e) {
                                            e.printStackTrace();
                                            fail();
                                        }
                                    }

                                    @Override
                                    public void onSyncFailed(String message) {
                                        fail(message);
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        fail();
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                fail();
            }
        }).get();
    }

    /**
     * Test only the recipe being inserted offline, excluding ingredients, tags and nutrition.
     */
    @Test
    public void OnlyMyRecipeInsertOffline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.onlyMyRecipeInsertOfflineArray(), new SyncCompleteCallback() {
                @Override
                public void onSyncComplete() {
                    try {
                        databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe response) {
                                MyRecipe myRecipe = createEmptyOnlineMyRecipe(response.getId(), response.getOnlineId());
                                // dateSynchronized sent from online
                                assertNotNull(response.getDateSynchronized());
                                // dateUpdated should only update on manual offline update
                                assertNull(response.getDateUpdated());
                                // assert that the online recipe data was properly inserted
                                assertThat(response, is(myRecipe));
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        fail();
                    }
                }
                @Override
                public void onSyncFailed(String message) {
                    fail(message);
                }
            });
        }).get();

    }

    /**
     * Test only the recipe being inserted offline, excluding ingredients, tags and nutrition.
     */
    @Test
    public void FullMyRecipeInsertOffline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.AllMyRecipeInsertOfflineArray(), new SyncCompleteCallback() {
                @Override
                public void onSyncComplete() {
                    try {
                        databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe recipeAfterSync) {
                                // ingredients
                                for (Ingredient ingredient : recipeAfterSync.getIngredients()) {
                                    assertNotNull(ingredient.getDateSynchronized());
                                    assertNull(ingredient.getDateUpdated());
                                    assertThat(ingredient, is(defaultOnlineIngredient(ingredient.getId(), ingredient.getOnlineId())));
                                }
                                // tags
                                for (RecipeTag tag : recipeAfterSync.getRecipeTags()) {
                                    assertNotNull(tag.getDateSynchronized());
                                    assertNull(tag.getDateUpdated());
                                    assertThat(tag, is (defaultOnlineTag(tag.getId(), tag.getOnlineId())));
                                }
                                // nutrition
                                for (Nutrition nutrition : recipeAfterSync.getNutritionList()) {
                                    assertNotNull(nutrition.getDateSynchronized());
                                    assertNull(nutrition.getDateUpdated());
                                    assertThat(nutrition, is(defaultOnlineNutrition(nutrition.getName())));
                                }
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        fail();
                    }
                }
                @Override
                public void onSyncFailed(String message) {
                    fail(message);
                }
            });
        }).get();
    }

    /**
     * Test only the recipe being updated offline, excluding ingredients, tags, and nutrition.
     */
    @Test
    public void OnlyMyRecipeUpdateOffline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createEmptyOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    try {
                        databaseAccess.fetchFullMyRecipe(response.getId(), new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe recipeBeforeSync) {
                                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.onlyMyRecipeUpdateOfflineNotDeletedPublicArray(recipeBeforeSync), new SyncCompleteCallback() {
                                    @Override
                                    public void onSyncComplete() {
                                        try {
                                            databaseAccess.fetchFullMyRecipe(recipeBeforeSync.getId(), new DbSingleCallback<MyRecipe>() {
                                                @Override
                                                public void onResponse(MyRecipe recipeAfterSync) {
                                                    assertNotNull(recipeAfterSync.getDateSynchronized());
                                                    // date updated should have not updated from updating on a sync update
                                                    assertEquals(recipeAfterSync.getDateUpdated().getTime(), recipeBeforeSync.getDateUpdated().getTime());
                                                    MyRecipe expectedRecipe = createEmptyOnlineMyRecipe(recipeAfterSync.getId(), recipeAfterSync.getOnlineId());
                                                    assertThat(recipeAfterSync, is(expectedRecipe));
                                                }
                                            });
                                        } catch (InterruptedException | ExecutionException e) {
                                            fail();
                                        }
                                    }

                                    @Override
                                    public void onSyncFailed(String message) {
                                        fail(message);
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        fail();
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();

    }

    /**
     * Test the recipe inner objects being updated offline, including ingredients, tags and nutrition.
     */
    @Test
    public void AllMyRecipeUpdateOffline() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            MyRecipe myRecipe = createEmptyOfflineMyRecipe(0, FakeSyncResponses.onlineId);
            try {
                databaseAccess.insertFullMyRecipe(myRecipe, response -> {
                    try {
                        databaseAccess.fetchFullMyRecipe(response.getId(), new DbSingleCallback<MyRecipe>() {
                            @Override
                            public void onResponse(MyRecipe recipeBeforeSync) {
                                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.onlyMyRecipeUpdateOfflineNotDeletedPublicArray(recipeBeforeSync), new SyncCompleteCallback() {
                                    @Override
                                    public void onSyncComplete() {
                                        try {
                                            databaseAccess.fetchFullMyRecipe(recipeBeforeSync.getId(), new DbSingleCallback<MyRecipe>() {
                                                @Override
                                                public void onResponse(MyRecipe recipeAfterSync) {
                                                    // ingredients
                                                    assertEquals(recipeAfterSync.getIngredients().size(), recipeBeforeSync.getIngredients().size());
                                                    for (int i = 0; i < recipeAfterSync.getIngredients().size(); i++) {
                                                        Ingredient ingredientAfterSync = recipeAfterSync.getIngredients().get(i);
                                                        Ingredient ingredientBeforeSync = recipeBeforeSync.getIngredients().get(i);
                                                        assertNotNull(ingredientAfterSync.getDateSynchronized());
                                                        assertEquals(ingredientBeforeSync.getDateUpdated().getTime(), ingredientBeforeSync.getDateUpdated().getTime());
                                                        assertThat(ingredientAfterSync, is(defaultOnlineIngredient(ingredientAfterSync.getId(), ingredientAfterSync.getOnlineId())));
                                                    }
                                                    // tags
                                                    assertEquals(recipeAfterSync.getRecipeTags().size(), recipeBeforeSync.getRecipeTags().size());
                                                    for (int i = 0; i < recipeAfterSync.getRecipeTags().size(); i++) {
                                                        RecipeTag tagAfterSync = recipeAfterSync.getRecipeTags().get(i);
                                                        RecipeTag tagBeforeSync = recipeBeforeSync.getRecipeTags().get(i);
                                                        assertNotNull(tagAfterSync.getDateSynchronized());
                                                        assertEquals(tagAfterSync.getDateUpdated().getTime(), tagBeforeSync.getDateUpdated().getTime());
                                                        assertThat(tagAfterSync, is(defaultOnlineTag(tagAfterSync.getId(), tagAfterSync.getOnlineId())));
                                                    }
                                                    // nutrition
                                                    assertEquals(recipeAfterSync.getNutritionList().size(), recipeBeforeSync.getNutritionList().size());
                                                    for (int i = 0; i < recipeAfterSync.getNutritionList().size(); i++) {
                                                        Nutrition nutritionAfterSync = recipeAfterSync.getNutritionList().get(i);
                                                        Nutrition nutritionBeforeSync = recipeBeforeSync.getNutritionList().get(i);
                                                        assertNotNull(nutritionAfterSync.getDateSynchronized());
                                                        assertEquals(nutritionAfterSync.getDateUpdated().getTime(), nutritionBeforeSync.getDateUpdated().getTime());
                                                        assertThat(nutritionAfterSync, is(defaultOnlineNutrition(nutritionAfterSync.getName())));
                                                    }
                                                }
                                            });
                                        } catch (InterruptedException | ExecutionException e) {
                                            fail();
                                        }
                                    }
                                    @Override
                                    public void onSyncFailed(String message) {
                                        fail(message);
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        fail();
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                fail();
            }
        }).get();
    }

    @Test
    public synchronized void insertIngredientInOfflineWithExistingNameInDb() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> databaseAccess.addIngredient(defaultOnlineIngredient(0, 1L), new DbSingleCallback<Ingredient>() {
            @Override
            public void onResponse(Ingredient response)  {
                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.AllMyRecipeInsertOfflineArray(), new SyncCompleteCallback() {
                    @Override
                    public void onSyncComplete() {
                        try {
                            databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                @Override
                                public void onResponse(MyRecipe recipeAfterSync)  {
                                    Ingredient ingredient = recipeAfterSync.getIngredients().get(0);
                                    // assert ingredient used the existing ingredient in db
                                    assertEquals(1, ingredient.getId());
                                    assertNotNull(ingredient.getDateSynchronized());
                                    // assert that ingredient was updated to match online one
                                    ingredient.setId(101);
                                    assertEquals(defaultOnlineIngredient(ingredient.getId(), FakeSyncResponses.onlineId), ingredient);
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            fail();
                        }
                    }
                    @Override
                    public void onSyncFailed(String message) {
                        fail(message);
                    }
                });
            }
        })).get();
    }

    @Test
    public synchronized void insertRecipeTagInOfflineWithExistingNameInDb() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> databaseAccess.addTag(defaultOnlineTag(0, 1L), new DbSingleCallback<RecipeTag>() {
            @Override
            public void onResponse(RecipeTag tag)  {
                syncRecipeFromResponse.syncMyRecipes(FakeSyncResponses.AllMyRecipeInsertOfflineArray(), new SyncCompleteCallback() {
                    @Override
                    public void onSyncComplete() {
                        try {
                            databaseAccess.fetchFullMyRecipe(1, new DbSingleCallback<MyRecipe>() {
                                @Override
                                public void onResponse(MyRecipe recipeAfterSync)  {
                                    RecipeTag tag = recipeAfterSync.getRecipeTags().get(0);
                                    // assert ingredient used the existing ingredient in db
                                    assertEquals(1, tag.getId());
                                    assertNotNull(tag.getDateSynchronized());
                                    // assert that ingredient was updated to match online one
                                    assertEquals(defaultOnlineTag(tag.getId(), FakeSyncResponses.onlineId), tag);
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            fail();
                        }
                    }
                    @Override
                    public void onSyncFailed(String message) {
                        fail(message);
                    }
                });
            }
        })).get();
    }

    @Test
    public void garbage() {
        org.junit.Assert.assertEquals(2, 2);
        assertEquals(2, 2);
        assertThat("123", is("123"));
    }

    /**
     * Create an offline recipe with no ingredient, tag, or nutrition
     * @param offlineId     Id of the recipe, 0 if none
     * @param onlineId      onlineId of recipe, null if none
     * @return              default offline recipe
     */
    private MyRecipe createEmptyOfflineMyRecipe(long offlineId, Long onlineId) {
        OfflineRecipe offlineRecipe = new OfflineRecipe.RecipeBuilder(offlineString)
                .setOnlineId(onlineId)
                .setId(offlineId)
                .setDescription(offlineString)
                .setInstructions(offlineString)
                .setPrepTime(offlineAmount)
                .setCookTime(offlineAmount)
                .setServingSize(offlineAmount)
                .build();
        return new MyRecipe(offlineRecipe, new AccessLevel(offlineAccessLevel));
    }

    /**
     * Create an online recipe with no ingredient, tag, or nutrition
     * @param offlineId     Id of the recipe, 0 if none
     * @param onlineId      onlineId of recipe, null if none
     * @return              default online recipe
     */
    private MyRecipe createEmptyOnlineMyRecipe(long offlineId, Long onlineId) {
        OfflineRecipe offlineRecipe = new OfflineRecipe.RecipeBuilder(FakeSyncResponses.onlineString)
                .setOnlineId(onlineId)
                .setId(offlineId)
                .setDescription(FakeSyncResponses.onlineString)
                .setInstructions(FakeSyncResponses.onlineString)
                .setPrepTime(FakeSyncResponses.onlineAmount)
                .setCookTime(FakeSyncResponses.onlineAmount)
                .setServingSize(FakeSyncResponses.onlineAmount)
                .build();
        return new MyRecipe(offlineRecipe, new AccessLevel(FakeSyncResponses.onlineAccessLevel));
    }

    /**
     * Create an full offline recipe with an ingredient, tag, and nutrition
     * @param offlineId     Id of the recipe, 0 if none
     * @param onlineId      onlineId of recipe, null if none
     * @return              default offline full recipe
     */
    private MyRecipe createFullOfflineMyRecipe(long offlineId, Long onlineId) {
        OfflineRecipe offlineRecipe = new OfflineRecipe.RecipeBuilder(offlineString)
                .setId(offlineId)
                .setOnlineId(onlineId)
                .setDescription(offlineString)
                .setInstructions(offlineString)
                .setPrepTime(offlineAmount)
                .setCookTime(offlineAmount)
                .setServingSize(offlineAmount)
                .setIngredients(Collections.singletonList(defaultOfflineIngredient(offlineId, onlineId)))
                .setRecipeTags(Collections.singletonList(defaultOfflineTag(offlineId, onlineId)))
                .setCalories(defaultOfflineNutrition(Nutrition.CALORIES))
                .build();
        return new MyRecipe(offlineRecipe, new AccessLevel(offlineAccessLevel));
    }

    /**
     * Create an full online recipe with an ingredient, tag, and nutrition
     * @param offlineId     Id of the recipe, 0 if none
     * @param onlineId      onlineId of recipe, null if none
     * @return              default offline full recipe
     */
    private MyRecipe createFullOnlineMyRecipe(long offlineId, Long onlineId) {
        OfflineRecipe offlineRecipe = new OfflineRecipe.RecipeBuilder(FakeSyncResponses.onlineString)
                .setDescription(FakeSyncResponses.onlineString)
                .setInstructions(FakeSyncResponses.onlineString)
                .setPrepTime(FakeSyncResponses.onlineAmount)
                .setCookTime(FakeSyncResponses.onlineAmount)
                .setServingSize(FakeSyncResponses.onlineAmount)
                .setIngredients(Collections.singletonList(defaultOnlineIngredient(offlineId, onlineId)))
                .setRecipeTags(Collections.singletonList(defaultOnlineTag(offlineId, onlineId)))
                .setCalories(defaultOnlineNutrition(Nutrition.CALORIES))
                .build();
        return new MyRecipe(offlineRecipe, new AccessLevel(FakeSyncResponses.onlineAccessLevel));
    }

    /**
     * Create a default offline ingredient
     * @param offlineId     Id of the ingredient, 0 if none
     * @param onlineId      onlineId of ingredient, null if none
     * @return              default offline ingredient
     */
    private Ingredient defaultOfflineIngredient(long offlineId, Long onlineId) {
        return new Ingredient.IngredientBuilder(offlineId, onlineId, offlineString)
                .setQuantity(offlineAmount)
                .setQuantityMeasId(offlineTypeId)
                .setFoodType(offlineTypeId)
                .build();
    }

    /**
     * Create a default online ingredient
     * @param offlineId     Id of the ingredient, 0 if none
     * @param onlineId      onlineId of ingredient, null if none
     * @return              default online ingredient
     */
    private Ingredient defaultOnlineIngredient(long offlineId, Long onlineId) {
        return new Ingredient.IngredientBuilder(offlineId, onlineId, FakeSyncResponses.onlineString)
                .setQuantity(FakeSyncResponses.onlineAmount)
                .setQuantityMeasId(FakeSyncResponses.onlineTypeId)
                .setFoodType(FakeSyncResponses.onlineTypeId)
                .build();
    }

    /**
     * Creates a default offline recipe tag
     * @param offlineId Id of the tag, 0 otherwise
     * @param onlineId  online id of the tag, null otherwise
     * @return          default offline tag
     */
    private RecipeTag defaultOfflineTag(long offlineId, Long onlineId) {
        return new RecipeTag.RecipeTagBuilder(offlineString)
                .setId(offlineId)
                .setOnlineId(onlineId).build();
    }

    /**
     * Creates a default online recipe tag
     * @param offlineId Id of the tag, 0 otherwise
     * @param onlineId  online id of the tag, null otherwise
     * @return          default online tag
     */
    private RecipeTag defaultOnlineTag(long offlineId, Long onlineId) {
        return new RecipeTag.RecipeTagBuilder(FakeSyncResponses.onlineString)
                .setId(offlineId)
                .setOnlineId(onlineId).build();
    }

    private Nutrition defaultOfflineNutrition(String name) {
        return new Nutrition.NutritionBuilder(name, offlineAmount, offlineTypeId).build();
    }
    private Nutrition defaultOnlineNutrition(String name) {
        return new Nutrition.NutritionBuilder(name, FakeSyncResponses.onlineAmount, FakeSyncResponses.onlineTypeId).build();
    }

    private final String offlineString = "offline";
    private final int offlineAmount = 1;
    private final long offlineTypeId = 1L;
    private final int offlineAccessLevel = 3;
}