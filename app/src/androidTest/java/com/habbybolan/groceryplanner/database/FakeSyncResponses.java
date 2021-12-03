package com.habbybolan.groceryplanner.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.sql.Timestamp;

public class FakeSyncResponses {

    // FAILED

    /*public static JsonArray onlyMyRecipeFailedError1Array(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = createFailedMessage("1");
        jsonObject.addProperty("id", myRecipe.getId());
        jsonObject.addProperty("online_id", myRecipe.getOnlineId());
        jsonObject.addProperty("name", myRecipe.getName());
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public static JsonArray allMyRecipeFailedError1Array() {
        // TODO:
        return null;
    }*/

    public static JsonArray onlyMyRecipeFailedError2aArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = createFailedMessage("2a");
        jsonObject.addProperty("id", myRecipe.getId());
        jsonObject.addProperty("online_id", myRecipe.getOnlineId());
        jsonObject.addProperty("name", myRecipe.getName());
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public static JsonArray allMyRecipeFailedError2aArray() {
        // TODO:
        return null;
    }

    public static JsonArray onlyMyRecipeFailedError2bArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = createFailedMessage("2b");
        jsonObject.addProperty("id", myRecipe.getId());
        jsonObject.addProperty("online_id", myRecipe.getOnlineId());
        jsonObject.addProperty("name", myRecipe.getName());
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public static JsonArray allMyRecipeFailedError2bArray() {
        // TODO:
        return null;
    }

    public static JsonArray onlyMyRecipeFailedError3Array(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = createFailedMessage("3");
        jsonObject.addProperty("id", myRecipe.getId());
        jsonObject.addProperty("online_id", myRecipe.getOnlineId());
        jsonObject.addProperty("name", myRecipe.getName());
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public static JsonArray allMyRecipeFailedError3Array() {
        // TODO:
        return null;
    }

    // INSERT_ONLINE
    public static JsonArray onlyMyRecipeInsertOnlineArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = new JsonObject();
        jsonMyRecipe.addProperty(SyncJSON.UpdateIdentifier, SyncJSON.OnlineUpdateIdentifier.INSERT_ONLINE.toString());
        jsonMyRecipe.addProperty("online_id", onlineId);
        jsonMyRecipe.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonMyRecipe.addProperty("id", myRecipe.getId());
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray allMyRecipeInsertOnlineArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineRecipeMinimal(myRecipe.getId(), onlineId, SyncJSON.OnlineUpdateIdentifier.INSERT_ONLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredientMinimal(ingredient.getId(), onlineId, SyncJSON.OnlineUpdateIdentifier.INSERT_ONLINE );
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTagMinimal(tag.getId(), onlineId, SyncJSON.OnlineUpdateIdentifier.INSERT_ONLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutritionMinimal(Nutrition.getIdFromName(nutrition.getName()), SyncJSON.OnlineUpdateIdentifier.INSERT_ONLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    // INSERT_OFFLINE
    public static JsonArray onlyMyRecipeInsertOfflineArray() {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(null, onlineId, false, false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeInsertOfflineArray() {
        JsonArray jsonArray = new JsonArray();
        // recipe
        JsonObject jsonMyRecipe = createOnlineMyRecipe(null, onlineId, false, false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        // ingredients
        JsonArray ingredientArray = new JsonArray();
        JsonObject jsonIngredient = createOnlineIngredient(null, onlineId, false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        ingredientArray.add(jsonIngredient);
        jsonMyRecipe.add("ingredients", ingredientArray);
        // tags
        JsonArray tagArray = new JsonArray();
        JsonObject jsonTag = createOnlineTag(null, onlineId, false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        tagArray.add(jsonTag);
        jsonMyRecipe.add("tags", tagArray);
        // nutrition
        JsonArray nutritionArray = new JsonArray();
        JsonObject jsonNutrition = createOnlineNutrition(1L, true,false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        nutritionArray.add(jsonNutrition);
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    // UPDATE_ONLINE
    public static JsonArray onlyMyRecipeUpdateOnlineArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = new JsonObject();
        jsonMyRecipe.addProperty(SyncJSON.UpdateIdentifier, SyncJSON.OnlineUpdateIdentifier.UPDATE_ONLINE.toString());
        jsonMyRecipe.addProperty("online_id", onlineId);
        jsonMyRecipe.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonMyRecipe.addProperty("id", myRecipe.getId());
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpdateOnlineArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineRecipeMinimal(myRecipe.getId(), myRecipe.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UPDATE_ONLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredientMinimal(ingredient.getId(), ingredient.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UPDATE_ONLINE );
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTagMinimal(tag.getId(), tag.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UPDATE_ONLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutritionMinimal(Nutrition.getIdFromName(nutrition.getName()), SyncJSON.OnlineUpdateIdentifier.UPDATE_ONLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    // UPDATE_OFFLINE
    public static JsonArray onlyMyRecipeUpdateOfflineNotDeletedArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), false, false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpdateOfflineNotDeletedArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), false, false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredient(ingredient.getId(), ingredient.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTag(tag.getId(), tag.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutrition(Nutrition.getIdFromName(nutrition.getName()), true,false, SyncJSON.OnlineUpdateIdentifier.INSERT_OFFLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray onlyMyRecipeUpdateOfflineNotDeletedPublicArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), false, true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpdateOfflineNotDeletedPublicArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), false, true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredient(ingredient.getId(), ingredient.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTag(tag.getId(), tag.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutrition(Nutrition.getIdFromName(nutrition.getName()), true,false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray onlyMyRecipeUpdateOfflineDeletedArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), true, false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpdateOfflineDeletedArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), true, false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredient(ingredient.getId(), ingredient.getOnlineId(), true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTag(tag.getId(), tag.getOnlineId(), true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutrition(Nutrition.getIdFromName(nutrition.getName()), true,true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray onlyMyRecipeUpdateOfflineDeletedPublicArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), true, true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpdateOfflineDeletedPublicArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineMyRecipe(myRecipe.getId(), myRecipe.getOnlineId(), false, true, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredient(ingredient.getId(), ingredient.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTag(tag.getId(), tag.getOnlineId(), false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutrition(Nutrition.getIdFromName(nutrition.getName()), true,false, SyncJSON.OnlineUpdateIdentifier.UPDATE_OFFLINE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    // UP_TO_DATE

    public static JsonArray onlyMyRecipeUpToDateArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineRecipeMinimal(myRecipe.getId(), myRecipe.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UP_TO_DATE);
        jsonMyRecipe.add("ingredients", new JsonArray());
        jsonMyRecipe.add("tags", new JsonArray());
        jsonMyRecipe.add("nutrition", new JsonArray());
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }

    public static JsonArray AllMyRecipeUpToDateArray(MyRecipe myRecipe) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonMyRecipe = createOnlineRecipeMinimal(myRecipe.getId(), myRecipe.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UP_TO_DATE);
        JsonArray ingredientArray = new JsonArray();
        for (Ingredient ingredient : myRecipe.getIngredients()) {
            JsonObject jsonIngredient = createOnlineIngredientMinimal(ingredient.getId(), ingredient.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UP_TO_DATE );
            ingredientArray.add(jsonIngredient);
        }
        jsonMyRecipe.add("ingredients", ingredientArray);
        JsonArray tagArray = new JsonArray();
        for (RecipeTag tag : myRecipe.getRecipeTags()) {
            JsonObject jsonTag = createOnlineTagMinimal(tag.getId(), tag.getOnlineId(), SyncJSON.OnlineUpdateIdentifier.UP_TO_DATE);
            tagArray.add(jsonTag);
        }
        jsonMyRecipe.add("tags", tagArray);
        JsonArray nutritionArray = new JsonArray();
        for (Nutrition nutrition : myRecipe.getNutritionList()) {
            JsonObject jsonNutrition = createOnlineNutritionMinimal(Nutrition.getIdFromName(nutrition.getName()), SyncJSON.OnlineUpdateIdentifier.UP_TO_DATE);
            nutritionArray.add(jsonNutrition);
        }
        jsonMyRecipe.add("nutrition", nutritionArray);
        jsonArray.add(jsonMyRecipe);
        return jsonArray;
    }


    // HELPER METHODS

    /**
     *
     * @param errorCode Web service error code sent when a recipe or inner object fails to sync/update/insert
     * @return          The error message that would send from the web service
     */
    private static JsonObject createFailedMessage(final String errorCode) {
        String failMessage;
        switch (errorCode) {
            case "1":
                failMessage = "Attempt to update a recipe with one that is out of date.";
                break;
            case "2a":
                failMessage = "User has read-only access to this recipe.";
                break;
            case "2b":
                failMessage =  "User doesn't have any access level to this recipe.";
                break;
            case "3":
                failMessage = "User has no read access to this recipe.";
                break;
            default:
                throw new Error("error code " + errorCode + " is not the proper value");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("update_identifier", "FAILED");
        jsonObject.addProperty("error_code", errorCode);
        jsonObject.addProperty("message", failMessage);
        return jsonObject;
    }

    /**
     * @return  full recipe json response from web service for notifying recipe needs to be updated or inserted online
     */
    private static JsonObject createOnlineMyRecipe(Long offlineId, Long onlineId, boolean isDeleted, boolean isPublic, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonMyRecipe = new JsonObject();
        if (onlineId != null) jsonMyRecipe.addProperty("online_id", onlineId);
        if (offlineId != null) jsonMyRecipe.addProperty("id", offlineId);
        jsonMyRecipe.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonMyRecipe.addProperty("recipe_name", onlineString);
        jsonMyRecipe.addProperty("prep_time", onlineAmount);
        jsonMyRecipe.addProperty("cook_time", onlineAmount);
        jsonMyRecipe.addProperty("serving_size", onlineAmount);
        jsonMyRecipe.addProperty("instructions", onlineString);
        jsonMyRecipe.addProperty("is_deleted", isDeleted);
        jsonMyRecipe.addProperty("description", onlineString);
        jsonMyRecipe.addProperty("is_public", isPublic);
        jsonMyRecipe.addProperty("access_level_id", onlineAccessLevel);
        jsonMyRecipe.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonMyRecipe;
    }

    /**
     * @return  recipe json response from web service for notifying recipe was updated or inserted online
     */
    private static JsonObject createOnlineRecipeMinimal(Long offlineId, Long onlineId, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonMyRecipe = new JsonObject();
        if (onlineId != null) jsonMyRecipe.addProperty("online_id", onlineId);
        if (offlineId != null) jsonMyRecipe.addProperty("id", offlineId);
        jsonMyRecipe.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonMyRecipe.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonMyRecipe;
    }

    /**
     * @return  full Ingredient json response from web service for notifying ingredient needs to be updated or inserted online
     */
    private static JsonObject createOnlineIngredient(Long offlineId, Long onlineId, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonIngredient = new JsonObject();
        if (offlineId != null) jsonIngredient.addProperty("id", offlineId);
        if (onlineId != null) jsonIngredient.addProperty("online_id", onlineId);
        jsonIngredient.addProperty("name", onlineString);
        jsonIngredient.addProperty("quantity", onlineAmount);
        jsonIngredient.addProperty("quantity_type", onlineTypeId);
        jsonIngredient.addProperty("is_deleted", isDeleted);
        jsonIngredient.addProperty("food_type_id", onlineTypeId);
        jsonIngredient.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonIngredient.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonIngredient;
    }

    /**
     * @return  ingredient json response from web service for notifying ingredient was updated or inserted online
     */
    private static JsonObject createOnlineIngredientMinimal(Long offlineId, Long onlineId, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonIngredient = new JsonObject();
        if (offlineId != null) jsonIngredient.addProperty("id", offlineId);
        if (onlineId != null) jsonIngredient.addProperty("online_id", onlineId);
        jsonIngredient.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonIngredient.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonIngredient;
    }

    /**
     * @return  full tag json response from web service for notifying tag needs to be updated or inserted online
     */
    private static JsonObject createOnlineTag(Long offlineId, Long onlineId, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonTag = new JsonObject();
        if (offlineId != null) jsonTag.addProperty("id", offlineId);
        if (onlineId != null) jsonTag.addProperty("online_id", onlineId);
        jsonTag.addProperty("title", onlineString);
        jsonTag.addProperty("is_deleted", isDeleted);
        jsonTag.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonTag.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonTag;
    }

    /**
     * @return  tag json response from web service for notifying tag was updated or inserted online
     */
    private static JsonObject createOnlineTagMinimal(Long offlineId, Long onlineId, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonTag = new JsonObject();
        if (offlineId != null) jsonTag.addProperty("id", offlineId);
        if (onlineId != null) jsonTag.addProperty("online_id", onlineId);
        jsonTag.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonTag.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonTag;
    }

    /**
     * @return  full nutrition json response from web service for notifying nutrition needs to be updated or inserted online
     */
    private static JsonObject createOnlineNutrition(Long nutritionId, boolean hasMeasurements, boolean isDeleted, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonNutrition = new JsonObject();
        jsonNutrition.addProperty("id", nutritionId);
        jsonNutrition.addProperty("is_deleted", isDeleted);
        jsonNutrition.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        if (hasMeasurements) jsonNutrition.addProperty("unit_of_measurement_id", onlineTypeId);
        else {
            Long val = null;
            jsonNutrition.addProperty("unit_of_measurement_id", val);
        }
        jsonNutrition.addProperty("amount", onlineAmount);
        jsonNutrition.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonNutrition;
    }

    /**
     * @return  nutrition json response from web service for notifying nutrition was updated or inserted online
     */
    private static JsonObject createOnlineNutritionMinimal(Long nutritionId, SyncJSON.OnlineUpdateIdentifier identifier) {
        JsonObject jsonNutrition = new JsonObject();
        jsonNutrition.addProperty("id", nutritionId);
        jsonNutrition.addProperty("date_synchronized", new Timestamp(System.currentTimeMillis()).toString());
        jsonNutrition.addProperty(SyncJSON.UpdateIdentifier, identifier.toString());
        return jsonNutrition;
    }

    public static final String onlineString = "online";
    public static final int onlineAmount = 100;
    public static final long onlineTypeId = 2;
    public static final long onlineId = 1000;
    public static final int onlineAccessLevel = 2;
}
