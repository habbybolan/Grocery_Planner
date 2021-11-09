package com.habbybolan.groceryplanner.models.lists;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * List representation of MyRecipe model.
 */
public class MyRecipeList {

    private List<MyRecipe> myRecipeList = new ArrayList<>();

    public MyRecipeList() {}

    public static class MyRecipeListBuilder {

        List<MyRecipe> myRecipeList = new ArrayList<>();

        public MyRecipeListBuilder() {}

        public MyRecipeListBuilder addRecipe(MyRecipe myRecipe) {
            myRecipeList.add(myRecipe);
            return this;
        }

        public MyRecipeList build() {
            MyRecipeList myRecipeList = new MyRecipeList();
            myRecipeList.myRecipeList.addAll(this.myRecipeList);
            return myRecipeList;
        }
    }

    public static class MyRecipeListSerializer implements JsonSerializer<MyRecipeList> {

        @Override
        public JsonElement serialize(MyRecipeList src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray jsonArray = new JsonArray();
            for (MyRecipe myRecipe : src.myRecipeList) {
                JsonObject jsonObject = myRecipe.createSyncJSON();
                jsonObject.addProperty("access_level", myRecipe.getAccessLevel().getId());
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }
    }
}
