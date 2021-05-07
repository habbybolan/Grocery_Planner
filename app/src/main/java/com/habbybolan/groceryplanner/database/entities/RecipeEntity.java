package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.sql.Timestamp;

@Entity(foreignKeys = {
        @ForeignKey(entity = RecipeCategoryEntity.class,
                parentColumns = "recipeCategoryId",
                childColumns = "recipe_category_id",
                onDelete = ForeignKey.SET_NULL),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "calories_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "fat_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "saturated_fat_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "carbohydrates_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "fiber_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "sugar_type"),
        @ForeignKey(entity = UnitOfMeasurementEntity.class,
                parentColumns = "id",
                childColumns = "protein_type")
})
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private long recipeId;
    @ColumnInfo(name="name")
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
    @ColumnInfo(name = "calories_type", index = true)
    private Long caloriesType;
    private int fat;
    @ColumnInfo(name = "fat_type", index = true)
    private Long fatType;
    @ColumnInfo(name = "saturated_fat")
    private int saturatedFat;
    @ColumnInfo(name = "saturated_fat_type", index = true)
    private Long saturatedFatType;
    private int carbohydrates;
    @ColumnInfo(name = "carbohydrates_type", index = true)
    private Long carbohydratesType;
    private int fiber;
    @ColumnInfo(name = "fiber_type", index = true)
    private Long fiberType;
    private int sugar;
    @ColumnInfo(name = "sugar_type", index = true)
    private Long sugarType;
    private int protein;
    @ColumnInfo(name = "protein_type", index = true)
    private Long proteinType;
    @ColumnInfo(name = "recipe_category_id", index = true)
    private Long recipeCategoryId;
    @ColumnInfo(name = "instructions")
    private String instructions;
    @ColumnInfo(name = "date_created", index = true)
    private Timestamp dateCreated;

    public RecipeEntity(long recipeId, String name, boolean isFavorite, String description, int prepTime, int cookTime, int servingSize, int calories, Long caloriesType,
                        int fat, Long fatType, int saturatedFat, Long saturatedFatType, int carbohydrates, Long carbohydratesType, int fiber,
                        Long fiberType, int sugar, Long sugarType, int protein, Long proteinType, Long recipeCategoryId, String instructions, Timestamp dateCreated) {
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
            caloriesType = recipe.getCalories().getMeasurementId();
        }
        if (recipe.getFat() != null) {
            fat = recipe.getFat().getAmount();
            fatType = recipe.getFat().getMeasurementId();
        }
        if (recipe.getSaturatedFat() != null) {
            saturatedFat = recipe.getSaturatedFat().getAmount();
            saturatedFatType = recipe.getSaturatedFat().getMeasurementId();
        }
        if (recipe.getCarbohydrates() != null) {
            carbohydrates = recipe.getCarbohydrates().getAmount();
            carbohydratesType = recipe.getCarbohydrates().getMeasurementId();
        }
        if (recipe.getFiber() != null) {
            fiber = recipe.getFiber().getAmount();
            fiberType = recipe.getFiber().getMeasurementId();
        }
        if (recipe.getSugar() != null) {
            sugar = recipe.getSugar().getAmount();
            sugarType = recipe.getSugar().getMeasurementId();
        }
        if (recipe.getProtein() != null) {
            protein = recipe.getProtein().getAmount();
            proteinType = recipe.getProtein().getMeasurementId();
        }
        recipeCategoryId = recipe.getCategoryId();
        instructions = recipe.getInstructions();
        dateCreated = recipe.getDateCreated();
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

    public Long getCaloriesType() {
        return caloriesType;
    }
    public Long getFatType() {
        return fatType;
    }
    public Long getSaturatedFatType() {
        return saturatedFatType;
    }
    public Long getCarbohydratesType() {
        return carbohydratesType;
    }
    public Long getFiberType() {
        return fiberType;
    }
    public Long getSugarType() {
        return sugarType;
    }
    public Long getProteinType() {
        return proteinType;
    }
    public Long getRecipeCategoryId() {
        return recipeCategoryId;
    }
    public String getInstructions() {
        return instructions;
    }
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public static String getNameColumn() {
        return "name";
    }
    public static String getDateColumn() {
        return "date_created";
    }
}
