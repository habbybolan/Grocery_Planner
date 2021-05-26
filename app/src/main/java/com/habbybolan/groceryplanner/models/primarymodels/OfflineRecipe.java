package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.sql.Timestamp;

public class OfflineRecipe extends Recipe implements OfflineIngredientHolder  {

    private Timestamp dateSynchronized;
    private long id;
    private boolean isUpdated;

    public final static String RECIPE = "recipe";

    protected OfflineRecipe() {}

    public OfflineRecipe(RecipeEntity recipeEntity) {
        name = recipeEntity.getName();
        id = recipeEntity.getRecipeId();
        onlineId = recipeEntity.getOnlineRecipeId();
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
        dateSynchronized = recipeEntity.getDateSynchronized();
        isUpdated = recipeEntity.getIsUpdated();
    }

    public OfflineRecipe(Parcel in) {
        super(in);
        dateSynchronized = (Timestamp) in.readSerializable();
        isUpdated = in.readInt() == 1;
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(dateSynchronized);
        dest.writeInt(isUpdated ? 1 : 0);
        dest.writeLong(id);
    }

    /**
     * Builder for Ingredient.
     */
    public static class RecipeBuilder implements RecipeBuilderInterface<RecipeBuilder> {

        protected String name;
        protected long id;
        protected Long onlineId;

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
        protected Timestamp dateSynchronized;

        protected Long categoryId;
        protected String instructions;

        protected int likes;
        protected boolean isUpdated;

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
        public RecipeBuilder setIsUpdated(boolean isUpdated) {
            this.isUpdated = isUpdated;
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
        public RecipeBuilder setDateSynchronized(Timestamp dateSynchronized) {
            this.dateSynchronized = dateSynchronized;
            return this;
        }
        @Override
        public RecipeBuilder setLikes(int likes) {
            this.likes = likes;
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
            offlineRecipe.isUpdated = isUpdated;
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
    public boolean getIsUpdated() {
        return isUpdated;
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
