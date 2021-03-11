package com.habbybolan.groceryplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Models Nutritional values of Recipes and Ingredients.
 */
public class Nutrition implements Parcelable {

    public static final String CALORIES = "Calories";
    public static final String FAT = "fat";
    public static final String SATURATED_FAT = "Saturated Fat";
    public static final String CARBOHYDRATES = "Carbohydrates";
    public static final String FIBRE = "fibre";
    public static final String SUGARS = "Sugars";
    public static final String PROTEIN = "Protein";

    private String name;
    // Amount of the nutrition value
    private int amount;
    // The type of measurement the amount is in (eg. g, mg, etc...)
    private String measurement;

    public Nutrition(String name, int amount, String measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }

    protected Nutrition(Parcel in) {
        name = in.readString();
        amount = in.readInt();
        measurement = in.readString();
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
        return new Nutrition(name, amount, measurement);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeString(measurement);
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
