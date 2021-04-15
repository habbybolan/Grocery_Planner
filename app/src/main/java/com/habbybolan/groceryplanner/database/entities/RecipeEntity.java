package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

@Entity(foreignKeys = @ForeignKey(entity = RecipeCategoryEntity.class,
                                parentColumns = "recipeCategoryId",
                                childColumns = "recipe_category_id",
                                onDelete = ForeignKey.SET_NULL))
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private long recipeId;
    private String name;
    @ColumnInfo(name="is_favorite")
    private boolean isFavorite;
    private String description;
    @ColumnInfo(name="prep_time")
    private int prepTime;
    @ColumnInfo(name="cook_time")
    private int cookTime;
    @ColumnInfo(name="serving_size")
    private int servingSize;
    private int calories;
    @ColumnInfo(name = "calories_type")
    private String caloriesType;
    private int fat;
    @ColumnInfo(name = "fat_type")
    private String fatType;
    @ColumnInfo(name = "saturated_fat")
    private int saturatedFat;
    @ColumnInfo(name = "saturated_fat_type")
    private String saturatedFatType;
    private int carbohydrates;
    @ColumnInfo(name = "carbohydrates_type")
    private String carbohydratesType;
    private int fiber;
    @ColumnInfo(name = "fiber_type")
    private String fiberType;
    private int sugar;
    @ColumnInfo(name = "sugar_type")
    private String sugarType;
    private int protein;
    @ColumnInfo(name = "protein_type")
    private String proteinType;
    @ColumnInfo(name = "recipe_category_id", index = true)
    private Long recipeCategoryId;
    @ColumnInfo(name = "instructions")
    private String instructions;

    public RecipeEntity(long recipeId, String name, boolean isFavorite, String description, int prepTime, int cookTime, int servingSize, int calories, String caloriesType,
                        int fat, String fatType, int saturatedFat, String saturatedFatType, int carbohydrates, String carbohydratesType, int fiber,
                        String fiberType, int sugar, String sugarType, int protein, String proteinType, Long recipeCategoryId, String instructions) {
        this.recipeId = recipeId;
        this.name = name;
        this.isFavorite = isFavorite;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servingSize = servingSize;
        this.calories = calories;
        this.caloriesType = caloriesType;
        this.fat = fat;
        this.fatType = fatType;
        this.saturatedFat = saturatedFat;
        this.saturatedFatType = saturatedFatType;
        this.carbohydrates = carbohydrates;
        this.carbohydratesType = carbohydratesType;
        this.fiber = fiber;
        this.fiberType = fiberType;
        this.sugar = sugar;
        this.sugarType = sugarType;
        this.protein = protein;
        this.proteinType = proteinType;
        this.recipeCategoryId = recipeCategoryId;
        this.instructions = instructions;
    }

    public RecipeEntity(Recipe recipe) {
        recipeId = recipe.getId();
        name = recipe.getName();
        isFavorite = recipe.getIsFavorite();

        description = recipe.getDescription();
        prepTime = recipe.getPrepTime();
        cookTime = recipe.getCookTime();
        servingSize = recipe.getServingSize();

        if (recipe.getCalories() != null) {
            calories = recipe.getCalories().getAmount();
            caloriesType = recipe.getCalories().getMeasurement();
        }
        if (recipe.getFat() != null) {
            fat = recipe.getFat().getAmount();
            fatType = recipe.getFat().getMeasurement();
        }
        if (recipe.getSaturatedFat() != null) {
            saturatedFat = recipe.getSaturatedFat().getAmount();
            saturatedFatType = recipe.getSaturatedFat().getMeasurement();
        }
        if (recipe.getCarbohydrates() != null) {
            carbohydrates = recipe.getCarbohydrates().getAmount();
            carbohydratesType = recipe.getCarbohydrates().getMeasurement();
        }
        if (recipe.getFiber() != null) {
            fiber = recipe.getFiber().getAmount();
            fiberType = recipe.getFiber().getMeasurement();
        }
        if (recipe.getSugar() != null) {
            sugar = recipe.getSugar().getAmount();
            sugarType = recipe.getSugar().getMeasurement();
        }
        if (recipe.getProtein() != null) {
            protein = recipe.getProtein().getAmount();
            proteinType = recipe.getProtein().getMeasurement();
        }
        recipeCategoryId = recipe.getCategoryId();
        instructions = recipe.getInstructions();
    }


    public long getRecipeId() {
        return recipeId;
    }
    public String getName() {
        return name;
    }
    public boolean getIsFavorite() {
        return isFavorite;
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

    public int getCalories() {
        return calories;
    }
    public int getFat() {
        return fat;
    }
    public int getSaturatedFat() {
        return saturatedFat;
    }
    public int getCarbohydrates() {
        return carbohydrates;
    }
    public int getFiber() {
        return fiber;
    }
    public int getSugar() {
        return sugar;
    }
    public int getProtein() {
        return protein;
    }

    public String getCaloriesType() {
        return caloriesType;
    }
    public String getFatType() {
        return fatType;
    }
    public String getSaturatedFatType() {
        return saturatedFatType;
    }
    public String getCarbohydratesType() {
        return carbohydratesType;
    }
    public String getFiberType() {
        return fiberType;
    }
    public String getSugarType() {
        return sugarType;
    }
    public String getProteinType() {
        return proteinType;
    }
    public Long getRecipeCategoryId() {
        return recipeCategoryId;
    }
    public String getInstructions() {
        return instructions;
    }
}
