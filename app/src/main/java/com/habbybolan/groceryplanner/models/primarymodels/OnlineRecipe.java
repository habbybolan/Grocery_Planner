package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OnlineRecipe extends Recipe{

    private OnlineRecipe() {
        super();
    }

    private List<RecipeTag> recipeTags = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();

    public OnlineRecipe(Parcel in) {
        super(in);
        in.readList(recipeTags, RecipeTag.class.getClassLoader());
        in.readList(ingredients, Ingredient.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(recipeTags);
        dest.writeList(ingredients);
    }

    public interface OnlineRecipeBuilderInterface<T> extends RecipeBuilderInterface<T> {
        T setTags(List<RecipeTag> recipeTags);
        T setIngredients(List<Ingredient> ingredients);
    }

    public static class OnlineRecipeBuilder implements OnlineRecipeBuilderInterface<OnlineRecipeBuilder> {

        protected String name;
        protected long id;

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

        protected Long categoryId;
        protected String instructions;

        protected int likes;

        private List<RecipeTag> recipeTags = new ArrayList<>();
        private List<Ingredient> ingredient = new ArrayList<>();

        public OnlineRecipeBuilder(@NonNull String name) {
            this.name = name;
        }

        @Override
        public OnlineRecipeBuilder setTags(List<RecipeTag> recipeTags) {
            this.recipeTags = recipeTags;
            return this;
        }

        @Override
        public OnlineRecipeBuilder setIngredients(List<Ingredient> ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        @Override
        public OnlineRecipeBuilder setId(long id) {
            this.id = id;
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
            onlineRecipe.id = id;

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
}
