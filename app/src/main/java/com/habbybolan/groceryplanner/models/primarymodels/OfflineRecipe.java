package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents all recipes stored offline, including User's own recipes, recipes they've been added to, and liked recipes.
 */
public class OfflineRecipe extends Recipe implements OfflineIngredientHolder, SyncJSON {

    protected long id;
    protected Timestamp dateSynchronized;
    protected Timestamp dateUpdated;
    protected Long categoryId;

    public final static String RECIPE = "recipe";

    private OfflineRecipe() {}

    public OfflineRecipe(String name, long id, Long onlineId, boolean isFavorite, String description, int prepTime, int cookTime, int servingSize, Long categoryId, String instructions,
                         Timestamp dateCreated, Timestamp dateSynchronized, List<RecipeTag> recipeTags, List<Ingredient> ingredients, List<Nutrition> nutritionList) {
        this.name = name;
        this.id = id;
        this.onlineId = onlineId;
        this.isFavorite = isFavorite;
        this.description = description;

        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servingSize = servingSize;
        this.categoryId = categoryId;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
        this.dateSynchronized = dateSynchronized;
        this.recipeTags = recipeTags;
        this.ingredients = ingredients;
        for (Nutrition nutrition : nutritionList) findNutrition(nutrition);
    }

    public OfflineRecipe(RecipeEntity recipeEntity) {
        name = recipeEntity.getName();
        id = recipeEntity.getRecipeId();
        onlineId = recipeEntity.getOnlineRecipeId();
        isFavorite = recipeEntity.getIsFavorite();
        description = recipeEntity.getDescription();

        prepTime = recipeEntity.getPrepTime();
        cookTime = recipeEntity.getCookTime();
        servingSize = recipeEntity.getServingSize();
        categoryId = recipeEntity.getRecipeCategoryId();
        instructions = recipeEntity.getInstructions();
        dateCreated = recipeEntity.getDateCreated();
        dateSynchronized = recipeEntity.getDateSynchronized();
    }

    public OfflineRecipe(OfflineRecipe offlineRecipe) {
        this.name = offlineRecipe.name;
        this.id = offlineRecipe.id;
        this.onlineId = offlineRecipe.onlineId;
        this.isFavorite = offlineRecipe.isFavorite;
        this.description = offlineRecipe.description;

        this.prepTime = offlineRecipe.prepTime;
        this.cookTime = offlineRecipe.cookTime;
        this.servingSize = offlineRecipe.servingSize;
        this.categoryId = offlineRecipe.categoryId;
        this.instructions = offlineRecipe.instructions;
        this.dateCreated = offlineRecipe.dateCreated;
        this.dateSynchronized = offlineRecipe.dateSynchronized;
        this.dateUpdated = offlineRecipe.dateUpdated;
        this.recipeTags = offlineRecipe.recipeTags;
        this.ingredients = offlineRecipe.ingredients;

        for (Nutrition nutrition : offlineRecipe.getNutritionList()) findNutrition(nutrition);
    }

    public OfflineRecipe(Parcel in) {
        super(in);
        dateSynchronized = (Timestamp) in.readSerializable();
        dateUpdated = (Timestamp) in.readSerializable();
        id = in.readLong();
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(dateSynchronized);
        dest.writeSerializable(dateUpdated);
        dest.writeLong(id);
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
    }

    @Override
    public JsonObject createSyncJSON() {
        return SyncJSON.getIsUpdate(dateUpdated, dateSynchronized) ? createSyncJSONUpdate() : createSyncJSONSync();
    }

    @Override
    public JsonObject createSyncJSONUpdate() {
        JsonObject json = new JsonObject();
        json.addProperty(SyncJSON.UpdateIdentifier, OfflineUpdateIdentifier.UPDATE.toString());
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("online_id", onlineId);
        json.addProperty("description", description);
        json.addProperty("prep_time", prepTime);
        json.addProperty("cook_time", cookTime);
        json.addProperty("serving_size", servingSize);
        json.addProperty("instructions", instructions);
        if (dateCreated != null) json.addProperty("date_created", dateCreated.toString());
        if (dateUpdated != null) json.addProperty("date_updated", dateUpdated.toString());
        if (dateSynchronized != null) json.addProperty("date_synchronized", dateSynchronized.toString());
        json.add("ingredients", createSyncJSONIngredients());
        json.add("tags", createSyncJSONTags());
        json.add("nutrition", createSyncJSONNutrition());
        return json;
    }

    @Override
    public JsonObject createSyncJSONSync() {
        JsonObject json = new JsonObject();
        json.addProperty(SyncJSON.UpdateIdentifier, OfflineUpdateIdentifier.UPDATE.toString());
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("online_id", onlineId);
        if (dateUpdated != null) json.addProperty("date_updated", dateUpdated.toString());
        if (dateSynchronized != null) json.addProperty("date_synchronized", dateSynchronized.toString());
        json.add("ingredients", createSyncJSONIngredients());
        json.add("tags", createSyncJSONTags());
        json.add("nutrition", createSyncJSONNutrition());
        return json;
    }

    private JsonArray createSyncJSONIngredients() {
        JsonArray json = new JsonArray();
        for (Ingredient ingredient : ingredients) {
            JsonObject jsonObject = SyncJSON.getIsUpdate(ingredient.getDateUpdated(), ingredient.getDateSynchronized()) ? ingredient.createSyncJSONUpdate() : ingredient.createSyncJSONSync();
            json.add(jsonObject);
        }
        return json;
    }

    private JsonArray createSyncJSONNutrition() {
        JsonArray json = new JsonArray();
        for (Nutrition nutrition : getNutritionList()) {
            JsonObject jsonObject = SyncJSON.getIsUpdate(nutrition.getDateUpdated(), nutrition.getDateSynchronized()) ? nutrition.createSyncJSONUpdate() : nutrition.createSyncJSONSync();
            json.add(jsonObject);
        }
        return json;
    }

    private JsonArray createSyncJSONTags() {
        JsonArray json = new JsonArray();
        for (RecipeTag tag : recipeTags) {
            JsonObject jsonObject = SyncJSON.getIsUpdate(tag.getDateUpdated(), tag.getDateSynchronized()) ? tag.createSyncJSONUpdate() : tag.createSyncJSONSync();
            json.add(jsonObject);
        }
        return json;
    }

    /**
     * Builder for Ingredient.
     */
    public static class RecipeBuilder implements RecipeBuilderInterface<RecipeBuilder> {

        private String name;
        private long id;
        private Long onlineId;

        private String description;
        private int prepTime;
        private int cookTime;
        private int servingSize;

        private Nutrition calories;
        private Nutrition fat;
        private Nutrition saturatedFat;
        private Nutrition carbohydrates;
        private Nutrition fiber;
        private Nutrition sugar;
        private Nutrition protein;

        private Timestamp dateCreated;
        private Timestamp dateSynchronized;

        private Long categoryId;
        private String instructions;

        private int likes;
        private Timestamp dateUpdated;
        private List<RecipeTag> recipeTags = new ArrayList<>();
        private List<Ingredient> ingredients = new ArrayList<>();

        public RecipeBuilder(@NonNull String name) {
            this.name = name;
        }

        public RecipeBuilder setId(long id) {
            this.id = id;
            return this;
        }
        public RecipeBuilder setOnlineId(Long onlineId) {
            this.onlineId = onlineId;
            return this;
        }
        public RecipeBuilder setDateUpdated(Timestamp dateUpdated) {
            this.dateUpdated = dateUpdated;
            return this;
        }
        @Override
        public RecipeBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        @Override
        public RecipeBuilder setPrepTime(int prepTime) {
           this.prepTime = prepTime;
            return this;
        }
        @Override
        public RecipeBuilder setCookTime(int cookTime) {
            this.cookTime = cookTime;
            return this;
        }
        @Override
        public RecipeBuilder setServingSize(int servingSize) {
            this.servingSize = servingSize;
            return this;
        }
        @Override
        public RecipeBuilder setCalories(Nutrition calories) {
            this.calories = calories;
            return this;
        }
        @Override
        public RecipeBuilder setFat(Nutrition fat) {
            this.fat = fat;
            return this;
        }
        @Override
        public RecipeBuilder setSaturatedFat(Nutrition saturatedFat) {
            this.saturatedFat = saturatedFat;
            return this;
        }
        @Override
        public RecipeBuilder setCarbohydrates(Nutrition carbohydrates) {
            this.carbohydrates = carbohydrates;
            return this;
        }
        @Override
        public RecipeBuilder setFiber(Nutrition fiber) {
            this.fiber = fiber;
            return this;
        }
        @Override
        public RecipeBuilder setSugar(Nutrition sugar) {
            this.sugar = sugar;
            return this;
        }
        @Override
        public RecipeBuilder setProtein(Nutrition protein) {
            this.protein = protein;
            return this;
        }
        public RecipeBuilder setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }
        @Override
        public RecipeBuilder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }
        @Override
        public RecipeBuilder setDateCreated(Timestamp dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public RecipeBuilder setDateSynchronized(Timestamp dateSynchronized) {
            this.dateSynchronized = dateSynchronized;
            return this;
        }

        @Override
        public RecipeBuilder setLikes(int likes) {
            this.likes = likes;
            return this;
        }

        @Override
        public RecipeBuilder setIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        @Override
        public RecipeBuilder setRecipeTags(List<RecipeTag> recipeTags) {
            this.recipeTags = recipeTags;
            return this;
        }


        public OfflineRecipe build() {
            OfflineRecipe offlineRecipe = new OfflineRecipe();
            offlineRecipe.name = name;
            offlineRecipe.id = id;
            offlineRecipe.onlineId = onlineId;

            offlineRecipe.description = description;
            offlineRecipe.prepTime = prepTime;
            offlineRecipe.cookTime = cookTime;
            offlineRecipe.servingSize = servingSize;

            offlineRecipe.calories = calories;
            offlineRecipe.fat = fat;
            offlineRecipe.saturatedFat = saturatedFat;
            offlineRecipe.carbohydrates = carbohydrates;
            offlineRecipe.fiber = fiber;
            offlineRecipe.sugar = sugar;
            offlineRecipe.protein = protein;
            offlineRecipe.categoryId = categoryId;
            offlineRecipe.instructions = instructions;

            offlineRecipe.dateCreated = dateCreated;
            offlineRecipe.dateSynchronized = dateSynchronized;
            offlineRecipe.likes = likes;
            offlineRecipe.dateUpdated = dateUpdated;
            offlineRecipe.ingredients = ingredients;
            offlineRecipe.recipeTags = recipeTags;
            return offlineRecipe;
        }
    }

    @Override
    public boolean isGrocery() {
        return false;
    }

    public static final Creator<OfflineRecipe> CREATOR = new Creator<OfflineRecipe>() {
        @Override
        public OfflineRecipe createFromParcel(Parcel in) {
            return new OfflineRecipe(in);
        }

        @Override
        public OfflineRecipe[] newArray(int size) {
            return new OfflineRecipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }
    public long getId() {
        return id;
    }
    public Timestamp getDateUpdated() {
        return dateUpdated;
    }
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
