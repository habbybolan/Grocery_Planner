package com.habbybolan.groceryplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Models Nutritional values of Recipes and Ingredients.
 */
public class Nutrition implements Parcelable {

    // Amount of the nutrition value
    private int amount;
    // The type of measurement the amount is in (eg. g, mg, etc...)
    private String measurement;

    public Nutrition(int amount, String measurement) {
        this.amount = amount;
        this.measurement = measurement;
    }

    protected Nutrition(Parcel in) {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeString(measurement);
    }

    public int getAmount() {
        return amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
