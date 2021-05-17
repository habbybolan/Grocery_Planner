package com.habbybolan.groceryplanner.online.discover.searchfilter;

import android.os.Parcel;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

/**
 * RecipeTags that hold an extra value for use in Discovering new recipes.
 * RecipeTag is either a pure RecipeTag search or a RecipeTag and recipe search.
 */
public class OnlineRecipeTag extends RecipeTag {

    private boolean isRecipeSearch = false;

    public OnlineRecipeTag(String title, boolean isRecipeSearch) {
        super(title);
        this.isRecipeSearch = isRecipeSearch;
    }

    public OnlineRecipeTag(Parcel in) {
        super(in);
        isRecipeSearch = in.readInt() == 1;
    }

    public static final Creator<OnlineRecipeTag> CREATOR = new Creator<OnlineRecipeTag>() {
        @Override
        public OnlineRecipeTag createFromParcel(Parcel in) {
            return new OnlineRecipeTag(in);
        }

        @Override
        public OnlineRecipeTag[] newArray(int size) {
            return new OnlineRecipeTag[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(isRecipeSearch ? 1 : 0);
    }

    public boolean getIsRecipeSearch() {
        return isRecipeSearch;
    }
}
