package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;

import java.sql.Timestamp;

/**
 * Models Nutritional values of Recipes and Ingredients.
 */
public class Nutrition extends MeasurementType {

    public static final String CALORIES = "Calories";
    public static final String FAT = "fat";
    public static final String SATURATED_FAT = "Saturated Fat";
    public static final String CARBOHYDRATES = "Carbohydrates";
    public static final String FIBRE = "fibre";
    public static final String SUGAR = "Sugar";
    public static final String PROTEIN = "Protein";

    public static final long CALORIES_ID = 1;
    public static final long FAT_ID = 2;
    public static final long SATURATED_FAT_ID = 3;
    public static final long CARBOHYDRATES_ID = 4;
    public static final long FIBER_ID = 5;
    public static final long SUGAR_ID = 6;
    public static final long PROTEIN_ID = 7;

    private String name;
    // Amount of the nutrition value
    private int amount;
    private Timestamp dateUpdated;
    private Timestamp dateSynchronized;

    private boolean isAddedToRecipe = true;

    public Nutrition(String name, int amount, @measurementIds Long measurementId, Timestamp dateUpdated, Timestamp dateSynchronized) {
        super(measurementId);
        this.name = name;
        this.amount = amount;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    public Nutrition(String name, int amount, @measurementIds Long measurementId) {
        super(measurementId);
        this.name = name;
        this.amount = amount;
    }

    public Nutrition(String name, int amount, @measurementIds Long measurementId, boolean isAddedToRecipe, Timestamp dateUpdated, Timestamp dateSynchronized) {
        super(measurementId);
        this.name = name;
        this.amount = amount;
        this.isAddedToRecipe = isAddedToRecipe;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    public Nutrition(long nutritionId, int amount, @measurementIds Long measurementId, Timestamp dateUpdated, Timestamp dateSynchronized) {
        super(measurementId);
        this.amount = amount;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
        switch ((int) nutritionId) {
            case (int) CALORIES_ID:
                name = CALORIES;
                break;
            case (int) FAT_ID:
                name = FAT;
                break;
            case (int) SATURATED_FAT_ID:
                name = SATURATED_FAT;
                break;
            case (int) CARBOHYDRATES_ID:
                name = CARBOHYDRATES;
                break;
            case (int) FIBER_ID:
                name = FIBRE;
                break;
            case (int) SUGAR_ID:
                name = SUGAR;
                break;
            case (int) PROTEIN_ID:
                name = PROTEIN;
                break;
            default:
                throw new IllegalArgumentException(nutritionId + " is not a valid nutrition id");
        }
    }

    protected Nutrition(Parcel in) {
        super(in);
        name = in.readString();
        amount = in.readInt();
        dateSynchronized = (Timestamp) in.readSerializable();
        dateUpdated = (Timestamp) in.readSerializable();
    }

    public static final Creator<Nutrition> CREATOR = new Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };

    @Override
    public Nutrition clone() {
        return new Nutrition(name, amount, measurementId, dateUpdated, dateSynchronized);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeSerializable(dateSynchronized);
        dest.writeSerializable(dateUpdated);
    }

    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public boolean getIsAddedToRecipe() {
        return isAddedToRecipe;
    }
    public Timestamp getDateUpdated() {
        return dateUpdated;
    }
    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setIsAddedToRecipe(boolean isAddedToRecipe) {
        this.isAddedToRecipe = isAddedToRecipe;
    }
    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    public void setDateSynchronized(Timestamp dateSynchronized) {
        this.dateSynchronized = dateSynchronized;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static String getNameFromId(long id) {
        switch ((int) id) {
            case 1:
                return CALORIES;
            case 2:
                return FAT;
            case 3:
                return SATURATED_FAT;
            case 4:
                return CARBOHYDRATES;
            case 5:
                return FIBRE;
            case 6:
                return SUGAR;
            default: return PROTEIN;
        }
    }

    public static long getIdFromFrom(String name) {
        switch (name) {
            case CALORIES:
                return CALORIES_ID;
            case FAT:
                return FAT_ID;
            case SATURATED_FAT:
                return SATURATED_FAT_ID;
            case CARBOHYDRATES:
                return CARBOHYDRATES_ID;
            case FIBRE:
                return FIBER_ID;
            case SUGAR:
                return SUGAR_ID;
            default: return PROTEIN_ID;
        }
    }
}
