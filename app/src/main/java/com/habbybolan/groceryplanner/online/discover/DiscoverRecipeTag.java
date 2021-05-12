package com.habbybolan.groceryplanner.online.discover;

import android.os.Parcel;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

/**
 * RecipeTags that hold an extra value for use in Discovering new recipes.
 * RecipeTag is either a pure RecipeTag search or a RecipeTag and recipe search.
 */
public class DiscoverRecipeTag extends RecipeTag {

    private boolean isRecipeSearch = false;

    public DiscoverRecipeTag(String title, boolean isRecipeSearch) {
        super(title);
        this.isRecipeSearch = isRecipeSearch;
    }

    public DiscoverRecipeTag(Parcel in) {
        super(in);
        isRecipeSearch = in.readInt() == 1;
    }

    public static final Creator<DiscoverRecipeTag> CREATOR = new Creator<DiscoverRecipeTag>() {
        @Override
        public DiscoverRecipeTag createFromParcel(Parcel in) {
            return new DiscoverRecipeTag(in);
        }

        @Override
        public DiscoverRecipeTag[] newArray(int size) {
            return new DiscoverRecipeTag[size];
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
