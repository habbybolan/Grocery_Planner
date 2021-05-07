package com.habbybolan.groceryplanner.models.combinedmodels;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeWithIngredient implements Parcelable {

    private float ingredientQuantity;
    private Long ingredientQuantityMeasId;
    private long recipeId;
    private String recipeName;
    private int recipeAmount;
    private String foodType;

    public RecipeWithIngredient(long recipeId, String recipeName, int recipeAmount, float ingredientQuantity,
                                Long ingredientQuantityMeasId) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeAmount = recipeAmount;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientQuantityMeasId = ingredientQuantityMeasId;
    }

    protected RecipeWithIngredient(Parcel in) {
        ingredientQuantity = in.readFloat();
        ingredientQuantityMeasId = in.readLong();
        recipeId = in.readLong();
        recipeName = in.readString();
        recipeAmount = in.readInt();
        foodType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(ingredientQuantity);
        dest.writeLong(ingredientQuantityMeasId);
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

    public float getIngredientQuantity() {
        return ingredientQuantity;
    }
    public Long getIngredientQuantityMeasId() {
        return ingredientQuantityMeasId;
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
