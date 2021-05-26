package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

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
    private Long onlineRecipeId;
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
    @ColumnInfo(defaultValue = "0")
    private int calories;
    @ColumnInfo(name = "calories_type", index = true)
    private Long caloriesType;
    @ColumnInfo(defaultValue = "0")
    private int fat;
    @ColumnInfo(name = "fat_type", index = true)
    private Long fatType;
    @ColumnInfo(defaultValue = "0", name = "saturated_fat")
    private int saturatedFat;
    @ColumnInfo(name = "saturated_fat_type", index = true)
    private Long saturatedFatType;
    @ColumnInfo(defaultValue = "0")
    private int carbohydrates;
    @ColumnInfo(name = "carbohydrates_type", index = true)
    private Long carbohydratesType;
    @ColumnInfo(defaultValue = "0")
    private int fiber;
    @ColumnInfo(name = "fiber_type", index = true)
    private Long fiberType;
    @ColumnInfo(defaultValue = "0")
    private int sugar;
    @ColumnInfo(name = "sugar_type", index = true)
    private Long sugarType;
    @ColumnInfo(defaultValue = "0")
    private int protein;
    @ColumnInfo(name = "protein_type", index = true)
    private Long proteinType;
    @ColumnInfo(name = "recipe_category_id", index = true)
    private Long recipeCategoryId;
    @ColumnInfo(name = "instructions")
    private String instructions;
    @ColumnInfo(name = "date_created", index = true)
    private Timestamp dateCreated;
    @ColumnInfo(name = "date_synchronized", index = true)
    private Timestamp dateSynchronized;
    @ColumnInfo(name = "is_updated")
    private boolean isUpdated;

    public RecipeEntity(long recipeId, Long onlineRecipeId, String name, boolean isFavorite, String description, int prepTime, int cookTime, int servingSize, int calories, Long caloriesType,
                        int fat, Long fatType, int saturatedFat, Long saturatedFatType, int carbohydrates, Long carbohydratesType, int fiber,
                        Long fiberType, int sugar, Long sugarType, int protein, Long proteinType, Long recipeCategoryId, String instructions, Timestamp dateCreated, Timestamp dateSynchronized, boolean isUpdated) {
        this.recipeId = recipeId;
        this.onlineRecipeId = onlineRecipeId;
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
        this.dateCreated = dateCreated;
        this.dateSynchronized = dateSynchronized;
        this.isUpdated = isUpdated;
    }

    public RecipeEntity(OfflineRecipe offlineRecipe) {
        recipeId = offlineRecipe.getId();
        onlineRecipeId = offlineRecipe.getOnlineId();
        name = offlineRecipe.getName();
        isFavorite = offlineRecipe.getIsFavorite();

        description = offlineRecipe.getDescription();
        prepTime = offlineRecipe.getPrepTime();
        cookTime = offlineRecipe.getCookTime();
        servingSize = offlineRecipe.getServingSize();

        if (offlineRecipe.getCalories() != null) {
            calories = offlineRecipe.getCalories().getAmount();
            caloriesType = offlineRecipe.getCalories().getMeasurementId();
        }
        if (offlineRecipe.getFat() != null) {
            fat = offlineRecipe.getFat().getAmount();
            fatType = offlineRecipe.getFat().getMeasurementId();
        }
        if (offlineRecipe.getSaturatedFat() != null) {
            saturatedFat = offlineRecipe.getSaturatedFat().getAmount();
            saturatedFatType = offlineRecipe.getSaturatedFat().getMeasurementId();
        }
        if (offlineRecipe.getCarbohydrates() != null) {
            carbohydrates = offlineRecipe.getCarbohydrates().getAmount();
            carbohydratesType = offlineRecipe.getCarbohydrates().getMeasurementId();
        }
        if (offlineRecipe.getFiber() != null) {
            fiber = offlineRecipe.getFiber().getAmount();
            fiberType = offlineRecipe.getFiber().getMeasurementId();
        }
        if (offlineRecipe.getSugar() != null) {
            sugar = offlineRecipe.getSugar().getAmount();
            sugarType = offlineRecipe.getSugar().getMeasurementId();
        }
        if (offlineRecipe.getProtein() != null) {
            protein = offlineRecipe.getProtein().getAmount();
            proteinType = offlineRecipe.getProtein().getMeasurementId();
        }
        recipeCategoryId = offlineRecipe.getCategoryId();
        instructions = offlineRecipe.getInstructions();
        dateCreated = offlineRecipe.getDateCreated();
        dateSynchronized = offlineRecipe.getDateSynchronized();
        isUpdated = offlineRecipe.getIsUpdated();
    }


    public long getRecipeId() {
        return recipeId;
    }
    public Long getOnlineRecipeId() {
        return onlineRecipeId;
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
    public boolean getIsUpdated() {
        return isUpdated;
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
    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }

    public static String getNameColumn() {
        return "name";
    }
    public static String getDateColumn() {
        return "date_created";
    }
}
