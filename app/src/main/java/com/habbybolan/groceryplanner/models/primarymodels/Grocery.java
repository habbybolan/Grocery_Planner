package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;

public class Grocery extends IngredientHolder {

    public final static String GROCERY = "grocery";

    public Grocery(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Grocery(String name) {
        this.name = name;
        id = 0;
    }

    public Grocery(GroceryEntity groceryEntity) {
        name = groceryEntity.getName();
        id = groceryEntity.getGroceryId();
    }

    public Grocery(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    @Override
    public boolean isGrocery() {
        return true;
    }

    public static final Creator<Grocery> CREATOR = new Creator<Grocery>() {
        @Override
        public Grocery createFromParcel(Parcel in) {
            return new Grocery(in);
        }

        @Override
        public Grocery[] newArray(int size) {
            return new Grocery[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }
}
