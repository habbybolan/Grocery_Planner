package com.habbybolan.groceryplanner.models.primarymodels;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Creates a MyRecipe instance to differentiate from LikedRecipe and add functionality to store the user's
 * access level to the recipe.
 */
public class MyRecipe extends OfflineRecipe {

    private AccessLevel accessLevel;

    public MyRecipe(OfflineRecipe offlineRecipe, AccessLevel accessLevel) {
        super(offlineRecipe);
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public static class MyRecipeSerializer implements JsonSerializer<MyRecipe> {

        @Override
        public JsonElement serialize(MyRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = src.createSyncJSON();
            jsonObject.addProperty("access_level", src.accessLevel.getId());
            return jsonObject;
        }
    }
}
