package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;

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

    public Nutrition(String name, int amount, @measurementIds Long measurementId) {
        super(measurementId);
        this.name = name;
        this.amount = amount;
    }

    public Nutrition(long nutritionId, int amount, @measurementIds Long measurementId) {
        super(measurementId);
        this.amount = amount;
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
        return new Nutrition(name, amount, measurementId);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeInt(amount);
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
