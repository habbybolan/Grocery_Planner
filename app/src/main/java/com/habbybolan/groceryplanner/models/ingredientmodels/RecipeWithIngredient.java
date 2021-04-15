package com.habbybolan.groceryplanner.models.ingredientmodels;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeWithIngredient implements Parcelable {

    private int ingredientQuantity;
    private String ingredientQuantityType;
    private long recipeId;
    private String recipeName;
    private int recipeAmount;
    private String foodType;

    public RecipeWithIngredient(long recipeId, String recipeName, int recipeAmount, int ingredientQuantity,
                                String ingredientQuantityType) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeAmount = recipeAmount;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientQuantityType = ingredientQuantityType;
    }

    protected RecipeWithIngredient(Parcel in) {
        ingredientQuantity = in.readInt();
        ingredientQuantityType = in.readString();
        recipeId = in.readLong();
        recipeName = in.readString();
        recipeAmount = in.readInt();
        foodType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ingredientQuantity);
        dest.writeString(ingredientQuantityType);
        dest.writeLong(recipeId);
        dest.writeString(recipeName);
        dest.writeInt(recipeAmount);
        dest.writeString(foodType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeWithIngredient> CREATOR = new Creator<RecipeWithIngredient>() {
        @Override
        public RecipeWithIngredient createFromParcel(Parcel in) {
            return new RecipeWithIngredient(in);
        }

        @Override
        public RecipeWithIngredient[] newArray(int size) {
            return new RecipeWithIngredient[size];
        }
    };

    public int getIngredientQuantity() {
        return ingredientQuantity;
    }
    public String getIngredientQuantityType() {
        return ingredientQuantityType;
    }
    public long getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public int getRecipeAmount() {
        return recipeAmount;
    }
}
