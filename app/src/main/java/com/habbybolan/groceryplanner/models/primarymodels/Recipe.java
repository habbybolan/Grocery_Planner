package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class Recipe extends IngredientHolder implements Parcelable {

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

    public Recipe() {}

    public Recipe(Parcel in) {
        super(in);
        name = in.readString();
        isFavorite = in.readByte() != 0;
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
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
        instructions = in.readString();
        likes = in.readInt();
        dateCreated = (Timestamp) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
        dest.writeString(instructions);
        dest.writeInt(likes);
        dest.writeSerializable(dateCreated);
    }

    public interface RecipeBuilderInterface<T> {
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
}
