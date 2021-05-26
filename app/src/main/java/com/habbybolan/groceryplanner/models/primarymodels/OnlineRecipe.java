package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OnlineRecipe extends Recipe {

    private Timestamp dateUpdated;
    private List<RecipeTag> recipeTags = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();

    private OnlineRecipe() {
        super();
    }

    public OnlineRecipe(Parcel in) {
        super(in);
        dateUpdated = (Timestamp) in.readSerializable();
        in.readList(recipeTags, RecipeTag.class.getClassLoader());
        in.readList(ingredients, Ingredient.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(dateUpdated);
        dest.writeList(recipeTags);
        dest.writeList(ingredients);
    }

    public static class OnlineRecipeBuilder implements RecipeBuilderInterface<OnlineRecipeBuilder> {

        protected String name;
        protected long onlineId;

        protected String description;
        protected int prepTime;
        protected int cookTime;
        protected int servingSize;

        protected Nutrition calories;
        protected Nutrition fat;
        protected Nutrition saturatedFat;
        protected Nutrition carbohydrates;
        protected Nutrition fiber;
        protected Nutrition sugar;
        protected Nutrition protein;

        protected Timestamp dateCreated;
        private Timestamp dateUpdated;

        protected Long categoryId;
        protected String instructions;

        protected int likes;

        private List<RecipeTag> recipeTags = new ArrayList<>();
        private List<Ingredient> ingredient = new ArrayList<>();

        public OnlineRecipeBuilder(@NonNull String name) {
            this.name = name;
        }

        public OnlineRecipeBuilder setTags(List<RecipeTag> recipeTags) {
            this.recipeTags = recipeTags;
            return this;
        }

        public OnlineRecipeBuilder setIngredients(List<Ingredient> ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public OnlineRecipeBuilder setOnlineId(Long onlineId) {
            this.onlineId = onlineId;
            return this;
        }
        public OnlineRecipeBuilder setDateUpdated(Timestamp dateUpdated) {
            this.dateUpdated = dateUpdated;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setPrepTime(int prepTime) {
            this.prepTime = prepTime;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setCookTime(int cookTime) {
            this.cookTime = cookTime;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setServingSize(int servingSize) {
            this.servingSize = servingSize;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setCalories(Nutrition calories) {
            this.calories = calories;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setFat(Nutrition fat) {
            this.fat = fat;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setSaturatedFat(Nutrition saturatedFat) {
            this.saturatedFat = saturatedFat;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setCarbohydrates(Nutrition carbohydrates) {
            this.carbohydrates = carbohydrates;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setFiber(Nutrition fiber) {
            this.fiber = fiber;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setSugar(Nutrition sugar) {
            this.sugar = sugar;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setProtein(Nutrition protein) {
            this.protein = protein;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setDateCreated(Timestamp dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }
        @Override
        public OnlineRecipeBuilder setLikes(int likes) {
            this.likes = likes;
            return this;
        }

        public OnlineRecipe build() {
            OnlineRecipe onlineRecipe = new OnlineRecipe();
            onlineRecipe.name = name;
            onlineRecipe.onlineId = onlineId;

            onlineRecipe.description = description;
            onlineRecipe.prepTime = prepTime;
            onlineRecipe.cookTime = cookTime;
            onlineRecipe.servingSize = servingSize;

            onlineRecipe.calories = calories;
            onlineRecipe.fat = fat;
            onlineRecipe.saturatedFat = saturatedFat;
            onlineRecipe.carbohydrates = carbohydrates;
            onlineRecipe.fiber = fiber;
            onlineRecipe.sugar = sugar;
            onlineRecipe.protein = protein;
            onlineRecipe.categoryId = categoryId;
            onlineRecipe.instructions = instructions;

            onlineRecipe.dateCreated = dateCreated;
            onlineRecipe.dateUpdated = dateUpdated;
            onlineRecipe.likes = likes;

            onlineRecipe.recipeTags = recipeTags;
            onlineRecipe.ingredients = ingredient;
            return onlineRecipe;
        }
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeTag> getRecipeTags() {
        return recipeTags;
    }

    public static final Creator<OnlineRecipe> CREATOR = new Creator<OnlineRecipe>() {
        @Override
        public OnlineRecipe createFromParcel(Parcel in) {
            return new OnlineRecipe(in);
        }

        @Override
        public OnlineRecipe[] newArray(int size) {
            return new OnlineRecipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public static class OnlineRecipeDeserialize implements JsonDeserializer<OnlineRecipe>{

        /**
         * Converts the JSON data from the web service into Recipe objects.
         * @param json  JSON Array data retrieved from the web service about the Recipe
         */
        @Override
        public OnlineRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonNutritionArray = jsonObject.has("nutrition") ? new JsonArray() : jsonObject.getAsJsonArray("nutrition");
            JsonArray jsonTagsArray = jsonObject.has("tags") ? new JsonArray() : jsonObject.getAsJsonArray("tags");
            JsonArray jsonIngredientsArray = jsonObject.has("ingredients") ? new JsonArray() : jsonObject.getAsJsonArray("ingredients");

            OnlineRecipe recipe = new OnlineRecipe.OnlineRecipeBuilder(jsonObject.get("recipe_name").getAsString())
                    .setOnlineId(jsonObject.get("recipe_id").getAsLong())
                    .setLikes(jsonObject.get("likes").getAsInt())
                    .setInstructions(jsonObject.get("instructions").getAsString())
                    .setDescription(jsonObject.get("description").getAsString())
                    .setDateCreated(getTimeStamp(jsonObject.get("date_added").getAsString()))
                    .setDateUpdated(jsonObject.has("date_updated") ? getTimeStamp(jsonObject.get("date_added").getAsString()) : null)
                    .setPrepTime(jsonObject.has("prep_time") ? -1 :jsonObject.get("prep_time").getAsInt())
                    .setCookTime(jsonObject.has("cook_time") ? -1 : jsonObject.get("cook_time").getAsInt())
                    .setServingSize(jsonObject.has("serving_size") ? -1 : jsonObject.get("serving_size").getAsInt())
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
            return recipe;
        }

        /**
         * Turns the JSONArray of ingredients to a list of ingredients.
         * @param jsonIngredientsArray  JSON array holding Ingredients information of the recipe
         * @return                      A list of Ingredients parsed from the JSONArray
         */
        private List<Ingredient> getIngredientsFrom(JsonArray jsonIngredientsArray) {
            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < jsonIngredientsArray.size(); i++) {
                JsonObject json = jsonIngredientsArray.get(i).getAsJsonObject();
                Ingredient ingredient = new Ingredient.IngredientBuilder(json.get("name").getAsString())
                        .setQuantity(json.get("quantity").getAsInt())
                        .setQuantityMeasId(json.get("measurement_id").getAsLong())
                        .setFoodType(json.get("food_type").getAsString())
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

        private List<RecipeTag> getTagsFromJSON(JsonArray jsonArray) {
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                recipeTags.add(new RecipeTag(jsonArray.get(i).getAsString()));
            }
            return recipeTags;
        }

        /**
         * Retrieve a recipe Nutrition from the jsonArray, removing it once the correct one is found.
         * @param jsonArray     Array holding the Nutrition JSON objects in the recipe
         * @param nutritionId   ID of the nutrition type to look for in array
         * @return              The nutrition object retrieved from the JSON object
         */
        private Nutrition getNutritionFromJSON(JsonArray jsonArray, long nutritionId) {
            // loop over array, looking for the JSON object inside the array with the correct nutritionId
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject json = jsonArray.get(i).getAsJsonObject();
                if (json.get("id").getAsLong() == nutritionId) {
                    Nutrition nutrition = new Nutrition(nutritionId, json.get("amount").getAsInt(), json.get("measurement_id").getAsLong());
                    // remove json object from array
                    jsonArray.remove(i);
                    return nutrition;
                }
            }
            // nutrition data not in recipe, return null
            return new Nutrition(Nutrition.getNameFromId(nutritionId), 0, (long) 0);
        }
    }
}
