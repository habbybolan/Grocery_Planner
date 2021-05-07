package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeTag implements Parcelable {

    private long id;
    private String title;

    public RecipeTag(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public RecipeTag(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected RecipeTag(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public static final Creator<RecipeTag> CREATOR = new Creator<RecipeTag>() {
        @Override
        public RecipeTag createFromParcel(Parcel in) {
            return new RecipeTag(in);
        }

        @Override
        public RecipeTag[] newArray(int size) {
            return new RecipeTag[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(title);
    }
}
