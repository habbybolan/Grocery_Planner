package com.habbybolan.groceryplanner.models.ingredientmodels;

import android.os.Parcel;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the Ingredients inside a Grocery list while representing
 */
public class GroceryIngredient extends Ingredient {

    public final static String GROCERY_INGREDIENT = "grocery_ingredient";

    // The recipes associated with this ingredient inside a particular grocery list
    private List<RecipeWithIngredient> recipeWithIngredients = new ArrayList<>();
    // true if the ingredient is checked off in the grocery list
    private boolean isChecked;
    // true if the ingredient has a direct relationship with the grocery list
    private boolean isDirectRelationship;

    public GroceryIngredient(Ingredient ingredient, boolean isChecked, boolean isDirectRelationship) {
        super(ingredient);
        this.isChecked = isChecked;
        this.isDirectRelationship = isDirectRelationship;
    }

    public GroceryIngredient(Parcel in) {
        super(in);
        isChecked = in.readByte() == 1;
        isDirectRelationship = in.readByte() == 1;
        in.readList(recipeWithIngredients, RecipeWithIngredient.class.getClassLoader());
    }

    /**
     * Adds a recipe associated with the ingredient.
     * @param recipeWithIngredient  Holds the recipe associated with the ingredient the the
     *                              amount of the Ingredient it adds
     */
    public void addRecipe(RecipeWithIngredient recipeWithIngredient) {
        recipeWithIngredients.add(recipeWithIngredient);
    }

    public List<RecipeWithIngredient> getRecipeWithIngredients() {
        return recipeWithIngredients;
    }

    public Ingredient getIngredient() {
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isDirectRelationship ? 1 : 0));
        dest.writeList(recipeWithIngredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroceryIngredient> CREATOR = new Creator<GroceryIngredient>() {
        @Override
        public GroceryIngredient createFromParcel(Parcel in) {
            return new GroceryIngredient(in);
        }

        @Override
        public GroceryIngredient[] newArray(int size) {
            return new GroceryIngredient[size];
        }
    };


    public boolean getIsChecked() {
        return isChecked;
    }
    public boolean getIsDirectRelationship() {
        return isDirectRelationship;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public void setIsDirectRelationship(boolean isDirectRelationship) {
        this.isDirectRelationship = isDirectRelationship;
    }
}
