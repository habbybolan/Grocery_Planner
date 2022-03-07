package com.habbybolan.groceryplanner.sync;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeTagEntity;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.secondarymodels.TimeModel;

import java.sql.Timestamp;

public class SyncRecipeFromResponse {

    private DatabaseAccess databaseAccess;

    public SyncRecipeFromResponse(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    /**
     * Takes online response with recipe and updates offline database
     * @param recipeArray   Serialized MyRecipes to send to online
     * @param callback      Callback once received response from web service
     */
    public synchronized void syncMyRecipes(JsonArray recipeArray, SyncCompleteCallback callback) {
        new Thread(() -> {
            for (JsonElement element : recipeArray) {
                JsonObject recipeObject = element.getAsJsonObject();
                applySyncedMyRecipe(recipeObject);
            }
            new Handler(Looper.getMainLooper()).post(() -> callback.onSyncComplete());
        }).start();
    }

    /**
     * Applies the synced my recipe from web service and commits and changes to offline database
     * @param recipeObject  Json Object of recipe to retrieved from web service
     */
    protected void applySyncedMyRecipe(JsonObject recipeObject) {
        // TODO: What to do with isFavorite value?
        // TODO: Put inside switch
        String identifier = recipeObject.get(SyncJSON.UpdateIdentifier).getAsString();
        Long recipeId;
        switch (identifier) {
            case "FAILED":
                // TODO:
                recipeId = null;
                break;
            case "INSERT_ONLINE":
                databaseAccess.syncRecipeUpdateSynchronized(recipeObject.get("id").getAsLong(), recipeObject.get("online_id").getAsLong(), TimeModel.getTimeStamp(recipeObject.get("date_synchronized").getAsString()));
                recipeId = recipeObject.get("id").getAsLong();
                break;
            case "INSERT_OFFLINE":
                RecipeEntity recipeEntityInsert = createRecipeEntity(recipeObject);
                recipeId = databaseAccess.syncMyRecipeInsert(recipeEntityInsert, recipeObject.get("access_level_id").getAsLong());
                break;
            case "UPDATE_ONLINE":
            case "UP_TO_DATE":
                databaseAccess.syncRecipeUpdateSynchronized(recipeObject.get("id").getAsLong(), null, TimeModel.getTimeStamp(recipeObject.get("date_synchronized").getAsString()));
                recipeId = recipeObject.get("id").getAsLong();
                break;
            case "UPDATE_OFFLINE":
                RecipeEntity recipeEntityUpdate = createRecipeEntity(recipeObject);
                databaseAccess.syncMyRecipeUpdate(recipeEntityUpdate, recipeObject.get("access_level_id").getAsLong());
                recipeId = recipeObject.get("id").getAsLong();
                break;
            default:
                throw new IllegalArgumentException("Identifier " + identifier + " is not valid for my recipes.");
        }
        // if null, then the sync failed
        if (recipeId != null) {
            applySyncedIngredient(recipeObject.get("ingredients").getAsJsonArray(), recipeId);
            applySyncedNutrition(recipeObject.get("nutrition").getAsJsonArray(), recipeId);
            applySyncedTags(recipeObject.get("tags").getAsJsonArray(), recipeId);
        }
    }

    /**
     * Applies the synced liked recipe from web service and commits and changes to offline database.
     * Can only pull data from the online database to update the online one.
     * @param recipeObject  Json Object of recipe to retrieved from web service
     */
    protected void applySyncedLikedRecipe(JsonObject recipeObject) {
        // TODO: What to do with isFavorite value?
        // TODO: Put inside switch
        String identifier = recipeObject.get(SyncJSON.UpdateIdentifier).getAsString();
        Long recipeId;
        switch (identifier) {
            case "FAILED":
                // TODO:
                recipeId = null;
                break;
            case "INSERT_OFFLINE":
                RecipeEntity recipeEntityInsert = createRecipeEntity(recipeObject);
                recipeId = databaseAccess.syncLikedRecipeInsert(recipeEntityInsert);
                break;
            case "UP_TO_DATE":
                databaseAccess.syncRecipeUpdateSynchronized(recipeObject.get("id").getAsLong(), null, TimeModel.getTimeStamp(recipeObject.get("date_synchronized").getAsString()));
                recipeId = recipeObject.get("id").getAsLong();
                break;
            case "UPDATE_OFFLINE":
                RecipeEntity recipeEntityUpdate = createRecipeEntity(recipeObject);
                databaseAccess.syncLikedRecipeUpdate(recipeEntityUpdate);
                recipeId = recipeObject.get("id").getAsLong();
                break;
            default:
                throw new IllegalArgumentException("Identifier " + identifier + " is not valid for liked recipes.");
        }
        if (recipeId != null) {
            applySyncedIngredient(recipeObject.get("ingredients").getAsJsonArray(), recipeId);
            applySyncedNutrition(recipeObject.get("nutrition").getAsJsonArray(), recipeId);
            applySyncedTags(recipeObject.get("tags").getAsJsonArray(), recipeId);
        }
    }

    private RecipeEntity createRecipeEntity(JsonObject recipeObject) {
        return new RecipeEntity(recipeObject.has("id") ? recipeObject.get("id").getAsLong() : 0, recipeObject.get("online_id").getAsLong(),
                recipeObject.get("recipe_name").getAsString(), false, recipeObject.get("description").getAsString(), recipeObject.get("prep_time").getAsInt(),
                recipeObject.get("cook_time").getAsInt(), recipeObject.get("serving_size").getAsInt(), null, recipeObject.get("instructions").getAsString(),
                new Timestamp(0), TimeModel.getTimeStamp(recipeObject.get("date_synchronized").getAsString()), new Timestamp(0), false);
    }

    /**
     * applies the synced ingredient from web service and commits any necessary changes to offline database.
     * @param ingredientArray   Json Array of ingredients retrieved from web service.
     * @param recipeId          Offline id of recipe ingredient is in
     */
    protected void applySyncedIngredient(JsonArray ingredientArray, long recipeId) {
        for (JsonElement element: ingredientArray) {
            JsonObject ingrObj = element.getAsJsonObject();
            String identifier = ingrObj.get(SyncJSON.UpdateIdentifier).getAsString();
            switch (identifier) {
                case "FAILED":
                    // TODO:
                    break;
                case "INSERT_ONLINE":
                    databaseAccess.syncIngredientUpdateSynchronized(recipeId, ingrObj.get("id").getAsLong(), ingrObj.get("online_id").getAsLong(), TimeModel.getTimeStamp(ingrObj.get("date_synchronized").getAsString()));
                    break;
                case "INSERT_OFFLINE":
                    IngredientEntity ingredientEntityInsert = new IngredientEntity(0, ingrObj.get("online_id").getAsLong(),
                            ingrObj.get("name").getAsString(), ingrObj.get("food_type_id").getAsLong());
                    databaseAccess.syncIngredientUpdateInsert(ingredientEntityInsert, recipeId, 0,
                            ingrObj.get("quantity").getAsFloat(), ingrObj.get("quantity_type").isJsonNull() ? null : ingrObj.get("quantity_type").getAsLong(),
                            TimeModel.getTimeStamp(ingrObj.get("date_synchronized").getAsString()), ingrObj.get("is_deleted").getAsBoolean(), ingrObj.get("food_type_id").getAsLong(), SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
                    break;
                case "UPDATE_ONLINE":
                case "UP_TO_DATE":
                    databaseAccess.syncIngredientUpdateSynchronized(recipeId, ingrObj.get("id").getAsLong(), null, TimeModel.getTimeStamp(ingrObj.get("date_synchronized").getAsString()));
                    break;
                case "UPDATE_OFFLINE":
                    IngredientEntity ingredientEntityUpdate = new IngredientEntity(ingrObj.get("id").getAsLong(), ingrObj.get("online_id").getAsLong(),
                            ingrObj.get("name").getAsString(), ingrObj.get("food_type_id").getAsLong());
                    databaseAccess.syncIngredientUpdateInsert(ingredientEntityUpdate, recipeId, ingrObj.get("id").getAsLong(),
                            ingrObj.get("quantity").getAsFloat(), ingrObj.get("quantity_type").isJsonNull() ? null : ingrObj.get("quantity_type").getAsLong(),
                            TimeModel.getTimeStamp(ingrObj.get("date_synchronized").getAsString()), ingrObj.get("is_deleted").getAsBoolean(), ingrObj.get("food_type_id").getAsLong(), SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
                    break;
                default:
                    throw new IllegalArgumentException("Identifier " + identifier + " is not valid.");
            }
        }
    }

    /**
     * Applies the synced tags from web service and commits any necessary changes to offline database.
     * @param tagArray   Json Array of tag retrieved from web service.
     * @param recipeId          Offline id of recipe tag is in
     */
    protected void applySyncedTags(JsonArray tagArray, long recipeId) {
        for (JsonElement element: tagArray) {
            JsonObject tag = element.getAsJsonObject();
            String identifier = tag.get(SyncJSON.UpdateIdentifier).getAsString();
            switch (identifier) {
                case "FAILED":
                    // TODO:
                    break;
                case "INSERT_ONLINE":
                    databaseAccess.syncRecipeTagUpdateSynchronized(recipeId, tag.get("id").getAsLong(), tag.get("online_id").getAsLong(), TimeModel.getTimeStamp(tag.get("date_synchronized").getAsString()));
                    break;
                case "INSERT_OFFLINE":
                    RecipeTagEntity recipeTagEntityInsert = new RecipeTagEntity(0, tag.get("online_id").getAsLong(), tag.get("title").getAsString());
                    databaseAccess.syncRecipeTagUpdateInsert(recipeTagEntityInsert, recipeId, 0,
                            TimeModel.getTimeStamp(tag.get("date_synchronized").getAsString()), tag.get("is_deleted").getAsBoolean(), SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
                    break;
                case "UPDATE_ONLINE":
                case "UP_TO_DATE":
                    databaseAccess.syncRecipeTagUpdateSynchronized(recipeId, tag.get("id").getAsLong(), null, TimeModel.getTimeStamp(tag.get("date_synchronized").getAsString()));
                    break;
                case "UPDATE_OFFLINE":
                    RecipeTagEntity recipeTagEntityUpdate = new RecipeTagEntity(tag.get("id").getAsLong(), tag.get("online_id").getAsLong(), tag.get("title").getAsString());
                    databaseAccess.syncRecipeTagUpdateInsert(recipeTagEntityUpdate, recipeId, tag.get("id").getAsLong(),
                            TimeModel.getTimeStamp(tag.get("date_synchronized").getAsString()), tag.get("is_deleted").getAsBoolean(), SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
                    break;
                default:
                    throw new IllegalArgumentException("Identifier " + identifier + " is not valid.");
            }
        }
    }

    /**
     * Applies the synced nutrition values from web service and commits any necessary changes to offline database.
     * @param nutritionArray   Json Array of nutrition retrieved from web service.
     * @param recipeId          Offline id of recipe nutrition is in
     */
    protected void applySyncedNutrition(JsonArray nutritionArray, long recipeId) {
        for (JsonElement element: nutritionArray) {
            JsonObject nutrition = element.getAsJsonObject();
            String identifier = nutrition.get(SyncJSON.UpdateIdentifier).getAsString();
            switch (identifier) {
                case "FAILED":
                    // TODO:
                    break;
                case "INSERT_ONLINE":
                case "UPDATE_ONLINE":
                case "UP_TO_DATE":
                    databaseAccess.syncNutritionUpdateSynchronized(recipeId, nutrition.get("id").getAsLong(), TimeModel.getTimeStamp(nutrition.get("date_synchronized").getAsString()));
                    break;
                case "INSERT_OFFLINE":
                    databaseAccess.syncNutritionUpdateInsert(recipeId, nutrition.get("id").getAsLong(), nutrition.get("amount").getAsInt(),
                            nutrition.get("unit_of_measurement_id").isJsonNull() ? null : nutrition.get("unit_of_measurement_id").getAsLong(),
                            TimeModel.getTimeStamp(nutrition.get("date_synchronized").getAsString()), nutrition.get("is_deleted").getAsBoolean(), SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
                    break;
                case "UPDATE_OFFLINE":
                    databaseAccess.syncNutritionUpdateInsert(recipeId, nutrition.get("id").getAsLong(), nutrition.get("amount").getAsInt(),
                            nutrition.get("unit_of_measurement_id").isJsonNull() ? null : nutrition.get("unit_of_measurement_id").getAsLong(),
                            TimeModel.getTimeStamp(nutrition.get("date_synchronized").getAsString()), nutrition.get("is_deleted").getAsBoolean(), SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
                    break;
                default:
                    throw new IllegalArgumentException("Identifier " + identifier + " is not valid.");
            }
        }
    }
}
