package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Recipe extends IngredientHolder {

    protected boolean isFavorite;

    protected String description;
    protected int prepTime = -1;
    protected int cookTime = -1;
    protected int servingSize = -1;

    protected Nutrition calories;
    protected Nutrition fat;
    protected Nutrition saturatedFat;
    protected Nutrition carbohydrates;
    protected Nutrition fiber;
    protected Nutrition sugar;
    protected Nutrition protein;
    protected Long categoryId;
    protected String instructions;
    protected Timestamp dateCreated;

    protected int likes;

    public final static String RECIPE = "recipe";

    protected Recipe() {}

    public Recipe(RecipeEntity recipeEntity) {
        name = recipeEntity.getName();
        id = recipeEntity.getRecipeId();
        isFavorite = recipeEntity.getIsFavorite();
        description = recipeEntity.getDescription();

        prepTime = recipeEntity.getPrepTime();
        cookTime = recipeEntity.getCookTime();
        servingSize = recipeEntity.getServingSize();
        calories = new Nutrition(Nutrition.CALORIES, recipeEntity.getCalories(), recipeEntity.getCaloriesType());
        fat = new Nutrition(Nutrition.FAT, recipeEntity.getFat(), recipeEntity.getFatType());
        saturatedFat = new Nutrition(Nutrition.SATURATED_FAT, recipeEntity.getSaturatedFat(), recipeEntity.getSaturatedFatType());
        carbohydrates = new Nutrition(Nutrition.CARBOHYDRATES, recipeEntity.getCarbohydrates(), recipeEntity.getCarbohydratesType());
        fiber = new Nutrition(Nutrition.FIBRE, recipeEntity.getFiber(), recipeEntity.getFiberType());
        sugar = new Nutrition(Nutrition.SUGAR, recipeEntity.getSugar(), recipeEntity.getSugarType());
        protein = new Nutrition(Nutrition.PROTEIN, recipeEntity.getProtein(), recipeEntity.getProteinType());
        categoryId = recipeEntity.getRecipeCategoryId();
        instructions = recipeEntity.getInstructions();
        dateCreated = recipeEntity.getDateCreated();
    }


    public Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        isFavorite = in.readByte() == 1;

        description = in.readString();
        prepTime = in.readInt();
        cookTime = in.readInt();
        servingSize = in.readInt();

        calories = in.readParcelable(Nutrition.class.getClassLoader());
        fat = in.readParcelable(Nutrition.class.getClassLoader());
        saturatedFat = in.readParcelable(Nutrition.class.getClassLoader());
        carbohydrates = in.readParcelable(Nutrition.class.getClassLoader());
        fiber = in.readParcelable(Nutrition.class.getClassLoader());
        sugar = in.readParcelable(Nutrition.class.getClassLoader());
        protein = in.readParcelable(Nutrition.class.getClassLoader());
        long idTemp = in.readLong();
        categoryId = idTemp != 0 ? idTemp : null;
        instructions = in.readString();
        dateCreated = (Timestamp) in.readSerializable();
        likes = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeByte((byte) (isFavorite ? 1 : 0));

        dest.writeString(description);
        dest.writeInt(prepTime);
        dest.writeInt(cookTime);
        dest.writeInt(servingSize);

        dest.writeParcelable(calories, flags);
        dest.writeParcelable(fat, flags);
        dest.writeParcelable(saturatedFat, flags);
        dest.writeParcelable(carbohydrates, flags);
        dest.writeParcelable(fiber, flags);
        dest.writeParcelable(sugar, flags);
        dest.writeParcelable(protein, flags);
        if (categoryId != null) dest.writeLong(categoryId);
        else dest.writeLong(0);
        dest.writeString(instructions);
        dest.writeSerializable(dateCreated);
        dest.writeInt(likes);
    }

    public List<Nutrition> getNutritionList() {
        List<Nutrition> nutritionList = new ArrayList<>();
        nutritionList.add(calories);
        nutritionList.add(fat);
        nutritionList.add(saturatedFat);
        nutritionList.add(carbohydrates);
        nutritionList.add(fiber);
        nutritionList.add(sugar);
        nutritionList.add(protein);
        return nutritionList;
    }

    /**
     * Given a list of nutritional facts, update the recipe with those facts
     * @param nutritionList The list of nutritional facts
     */
    public void updateNutrition(List<Nutrition> nutritionList) {
        for (Nutrition nutrition : nutritionList) {
            switch(nutrition.getName()) {
                case Nutrition.CALORIES:
                    calories = nutrition;
                    break;
                case Nutrition.FAT:
                    fat = nutrition;
                    break;
                case Nutrition.SATURATED_FAT:
                    saturatedFat = nutrition;
                    break;
                case Nutrition.CARBOHYDRATES:
                    carbohydrates = nutrition;
                    break;
                case Nutrition.FIBRE:
                    fiber = nutrition;
                    break;
                case Nutrition.SUGAR:
                    sugar = nutrition;
                    break;
                case Nutrition.PROTEIN:
                    protein = nutrition;
                    break;
            }
        }
    }

    public interface RecipeBuilderInterface<T> {
        T setId(long id);
        T setDescription(String description);
        T setPrepTime(int prepTime);
        T setCookTime(int cookTime);
        T setServingSize(int servingSize);
        T setCalories(Nutrition calories);
        T setFat(Nutrition fat);
        T setSaturatedFat(Nutrition saturatedFat);
        T setCarbohydrates(Nutrition carbohydrates);
        T setFiber(Nutrition fiber);
        T setSugar(Nutrition sugar);
        T setProtein(Nutrition protein);
        T setCategoryId(Long categoryId);
        T setInstructions(String instructions);
        T setDateCreated(Timestamp dateCreated);
        T setLikes(int likes);
    }

    /**
     * Builder for Ingredient.
     */
    public static class RecipeBuilder implements RecipeBuilderInterface<RecipeBuilder> {

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

        public RecipeBuilder(@NonNull String name) {
            this.name = name;
        }
        @Override
        public RecipeBuilder setId(long id) {
            this.id = id;
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
        @Override
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
        @Override
        public RecipeBuilder setLikes(int likes) {
            this.likes = likes;
            return this;
        }


        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.name = name;
            recipe.id = id;

            recipe.description = description;
            recipe.prepTime = prepTime;
            recipe.cookTime = cookTime;
            recipe.servingSize = servingSize;

            recipe.calories = calories;
            recipe.fat = fat;
            recipe.saturatedFat = saturatedFat;
            recipe.carbohydrates = carbohydrates;
            recipe.fiber = fiber;
            recipe.sugar = sugar;
            recipe.protein = protein;
            recipe.categoryId = categoryId;
            recipe.instructions = instructions;

            recipe.dateCreated = dateCreated;
            recipe.likes = likes;
            return recipe;
        }
    }

    @Override
    public boolean isGrocery() {
        return false;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDescription() {
        return description;
    }
    public int getPrepTime() {
        return prepTime;
    }
    public int getCookTime() {
        return cookTime;
    }
    public int getServingSize() {
        return servingSize;
    }
    public Nutrition getCalories() {
        return calories;
    }
    public Nutrition getFat() {
        return fat;
    }
    public Nutrition getSaturatedFat() {
        return saturatedFat;
    }
    public Nutrition getCarbohydrates() {
        return carbohydrates;
    }
    public Nutrition getFiber() {
        return fiber;
    }
    public Nutrition getSugar() {
        return sugar;
    }
    public Nutrition getProtein() {
        return protein;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public String getInstructions() {
        return instructions;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public Timestamp getDateCreated() {
        return dateCreated;
    }
    public int getLikes() {
        return likes;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }
    public void setCalories(Nutrition calories) {
        this.calories = calories;
    }
    public void setFat(Nutrition fat) {
        this.fat = fat;
    }
    public void setSaturatedFat(Nutrition saturatedFat) {
        this.saturatedFat = saturatedFat;
    }
    public void setCarbohydrates(Nutrition carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    public void setFiber(Nutrition fiber) {
        this.fiber = fiber;
    }
    public void setSugar(Nutrition sugar) {
        this.sugar = sugar;
    }
    public void setProtein(Nutrition protein) {
        this.protein = protein;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
