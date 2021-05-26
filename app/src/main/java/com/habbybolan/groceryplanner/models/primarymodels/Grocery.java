package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;

import java.sql.Timestamp;

public class Grocery extends IngredientHolder implements OfflineIngredientHolder {

    private Timestamp dateSynchronized;
    private long id;

    public final static String GROCERY = "grocery";

    public Grocery(String name, long id, Long onlineId, Timestamp dateSynchronized) {
        this.name = name;
        this.id = id;
        this.onlineId = onlineId;
        this.dateSynchronized = dateSynchronized;
    }

    public Grocery(String name) {
        this.name = name;
        id = 0;
    }

    public Grocery(GroceryEntity groceryEntity) {
        name = groceryEntity.getName();
        id = groceryEntity.getGroceryId();
        onlineId = groceryEntity.getOnlineGroceryId();
        dateSynchronized = groceryEntity.getDateSynchronized();
    }

    public Grocery(Parcel in) {
        super(in);
        id = in.readLong();
        name = in.readString();
        dateSynchronized = (Timestamp) in.readSerializable();
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
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeSerializable(dateSynchronized);
    }

    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }
    public long getId() {
        return id;
    }
}
