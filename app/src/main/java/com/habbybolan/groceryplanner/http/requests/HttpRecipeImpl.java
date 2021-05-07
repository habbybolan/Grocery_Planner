package com.habbybolan.groceryplanner.http.requests;

import android.content.Context;

import com.habbybolan.groceryplanner.WebServiceCallback;
import com.habbybolan.groceryplanner.http.HttpRequestImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.webmodels.WebServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpRecipeImpl extends HttpRequestImpl implements HttpRecipe{

    public HttpRecipeImpl(Context context) {
        super(context);
    }

    @Override
    public void getRecipesNew(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<WebServiceResponse<OnlineRecipe>> task = new Callable<WebServiceResponse<OnlineRecipe>>() {
            @Override
            public WebServiceResponse<OnlineRecipe> call() throws Exception {

                try {
                    HttpURLConnection connection = getHttpConnection("/api/recipes/date?offset=" + offset + "&numRows=" + numRows + "&sortType=ASC", "GET", "");
                    if (connection.getResponseCode() == 200) {
                        JSONArray jsonArray = connectAndReadResponseGET(connection);
                        List<OnlineRecipe> recipes = createRecipes(jsonArray);
                        return new WebServiceResponse<>(recipes, 200);
                    } else {
                        return new WebServiceResponse<>(connection.getResponseCode());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    // todo: what should be returned here to signal exception was thrown?
                    return new WebServiceResponse<>(404);
                }
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WebServiceResponse<OnlineRecipe>> futureTask = executorService.submit(task);
        WebServiceResponse<OnlineRecipe> webResponse = futureTask.get();
        callback.onResponse(webResponse.getItems(), webResponse.getResponseCode());

    }

    @Override
    public void getRecipesTrending(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<WebServiceResponse<OnlineRecipe>> task = new Callable<WebServiceResponse<OnlineRecipe>>() {
            @Override
            public WebServiceResponse<OnlineRecipe> call() throws Exception {

                try {
                    HttpURLConnection connection = getHttpConnection("/api/recipes/trending?offset=" + offset + "&numRows=" + numRows, "GET", "");
                    if (connection.getResponseCode() == 200) {
                        JSONArray jsonArray = connectAndReadResponseGET(connection);
                        List<OnlineRecipe> recipes = createRecipes(jsonArray);
                        return new WebServiceResponse<>(recipes, 200);
                    } else {
                        return new WebServiceResponse<>(connection.getResponseCode());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    // todo: what should be returned here to signal exception was thrown?
                    return new WebServiceResponse<>(404);
                }
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WebServiceResponse<OnlineRecipe>> futureTask = executorService.submit(task);
        WebServiceResponse<OnlineRecipe> webResponse = futureTask.get();
        callback.onResponse(webResponse.getItems(), webResponse.getResponseCode());
    }

    @Override
    public void getRecipesSaved(int offset, int numRows, WebServiceCallback<OnlineRecipe> callback) throws ExecutionException, InterruptedException {
        Callable<WebServiceResponse<OnlineRecipe>> task = new Callable<WebServiceResponse<OnlineRecipe>>() {
            @Override
            public WebServiceResponse<OnlineRecipe> call() throws Exception {

                try {// todo: get user id from savedPreferences of logged in user
                    HttpURLConnection connection = getHttpConnection("/api/recipes/users/1?offset=" + offset + "&numRows=" + numRows, "GET", "");
                    if (connection.getResponseCode() == 200) {
                        JSONArray jsonArray = connectAndReadResponseGET(connection);
                        List<OnlineRecipe> recipes = createRecipes(jsonArray);
                        return new WebServiceResponse<>(recipes, 200);
                    } else {
                        return new WebServiceResponse<>(connection.getResponseCode());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    // todo: what should be returned here to signal exception was thrown?
                    return new WebServiceResponse<>(404);
                }
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WebServiceResponse<OnlineRecipe>> futureTask = executorService.submit(task);
        WebServiceResponse<OnlineRecipe> webResponse = futureTask.get();
        callback.onResponse(webResponse.getItems(), webResponse.getResponseCode());
    }

    /**
     * Converts the JSON data from the web service into Recipe objects and notifies the conversion is done
     * through the callback.
     * @param jsonArray JSON Array data retrieved from the web service about the Recipe
     */
    private List<OnlineRecipe> createRecipes(JSONArray jsonArray) throws JSONException {
        List<OnlineRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray jsonNutritionArray = jsonObject.isNull("nutrition") ? new JSONArray() : new JSONArray(jsonObject.getString("nutrition"));
            JSONArray jsonTagsArray = jsonObject.isNull("tags") ? new JSONArray() : new JSONArray(jsonObject.getString("tags"));
            JSONArray jsonIngredientsArray = jsonObject.isNull("ingredients") ? new JSONArray() : new JSONArray(jsonObject.getString("ingredients"));

            OnlineRecipe recipe = new OnlineRecipe.OnlineRecipeBuilder(jsonObject.getString("recipe_name"))
                    .setId(jsonObject.getInt("recipe_id"))
                    .setLikes(jsonObject.getInt("likes"))
                    .setInstructions(jsonObject.getString("instructions"))
                    .setDescription(jsonObject.getString("description"))
                    .setDateCreated(getTimeStamp(jsonObject.getString("date_added")))
                    .setPrepTime(jsonObject.isNull("prep_time") ? -1 :jsonObject.getInt("prep_time"))
                    .setCookTime(jsonObject.isNull("cook_time") ? -1 : jsonObject.getInt("cook_time"))
                    .setServingSize(jsonObject.isNull("serving_size") ? -1 : jsonObject.getInt("serving_size"))
                    // nutrition
                    .setCalories(getNutritionFromJSON(jsonNutritionArray, Nutrition.CALORIES_ID))
                    .setFat(getNutritionFromJSON(jsonNutritionArray, Nutrition.FAT_ID))
                    .setSaturatedFat(getNutritionFromJSON(jsonNutritionArray, Nutrition.SATURATED_FAT_ID))
                    .setCarbohydrates(getNutritionFromJSON(jsonNutritionArray, Nutrition.CARBOHYDRATES_ID))
                    .setFiber(getNutritionFromJSON(jsonNutritionArray, Nutrition.FIBER_ID))
                    .setSugar(getNutritionFromJSON(jsonNutritionArray, Nutrition.SUGAR_ID))
                    .setProtein(getNutritionFromJSON(jsonNutritionArray, Nutrition.PROTEIN_ID))
                    // lists
                    .setTags(getTagsFromJSON(jsonTagsArray))
                    .setIngredients(getIngredientsFrom(jsonIngredientsArray))
                    .build();
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * Turns the JSONArray of ingredients to a list of ingredients.
     * @param jsonIngredientsArray  JSON array holding Ingredients information of the recipe
     * @return                      A list of Ingredients parsed from the JSONArray
     */
    private List<Ingredient> getIngredientsFrom(JSONArray jsonIngredientsArray) throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < jsonIngredientsArray.length(); i++) {
            JSONObject json = jsonIngredientsArray.getJSONObject(i);
            Ingredient ingredient = new Ingredient.IngredientBuilder(json.getString("name"))
                    .setQuantity(json.getInt("quantity"))
                    .setQuantityMeasId(json.getLong("measurement_id"))
                    .setFoodType(json.getString("food_type"))
                    .build();
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    /**
     * Converts the mssql datetime2 string into proper format for TimeStamp and return the created timestamp.
     * @param datetime2String   String date of DateTime2
     * @return                  The TimeStamp date
     */
    private Timestamp getTimeStamp(String datetime2String) {
        // todo: does this work in every case?
        String timeStampString = datetime2String.replace('T', ' ');
        return Timestamp.valueOf(timeStampString);
    }

    private List<RecipeTag> getTagsFromJSON(JSONArray jsonArray) throws JSONException {
        List<RecipeTag> recipeTags = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            recipeTags.add(new RecipeTag(jsonArray.getString(i)));
        }
        return recipeTags;
    }

    /**
     * Retrieve a recipe Nutrition from the jsonArray, removing it once the correct one is found.
     * @param jsonArray     Array holding the Nutrition JSON objects in the recipe
     * @param nutritionId   ID of the nutrition type to look for in array
     * @return              The nutrition object retrieved from the JSON object
     */
    private Nutrition getNutritionFromJSON(JSONArray jsonArray, long nutritionId) throws JSONException {
        // loop over array, looking for the JSON object inside the array with the correct nutritionId
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            if (json.getLong("id") == nutritionId) {
                Nutrition nutrition = new Nutrition(nutritionId, json.getInt("amount"), json.getLong("measurement_id"));
                // remove json object from array
                jsonArray.remove(i);
                return nutrition;
            }
        }
        // nutrition data not in recipe, return null
        return null;
    }
}
