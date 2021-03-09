package com.habbybolan.groceryplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.habbybolan.groceryplanner.database.entities.RecipeCategoryEntity;

public class RecipeCategory extends Category implements Parcelable{

    public static String RECIPE_CATEGORY = "recipe_category";

    public RecipeCategory(String name, long id) {
        this.name = name;
        this.id = id;
    }
    public RecipeCategory(String name) {
        this.name = name;
    }
    public RecipeCategory(RecipeCategoryEntity recipeCategoryEntity) {
        id = recipeCategoryEntity.getRecipeCategoryId();
        name = recipeCategoryEntity.getName();
    }
    public RecipeCategory(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public static final Creator<RecipeCategory> CREATOR = new Creator<RecipeCategory>() {
        @Override
        public RecipeCategory createFromParcel(Parcel in) {
            return new RecipeCategory(in);
        }

        @Override
        public RecipeCategory[] newArray(int size) {
            return new RecipeCategory[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }
}
