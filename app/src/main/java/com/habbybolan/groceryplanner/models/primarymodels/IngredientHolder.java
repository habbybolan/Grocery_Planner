package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcelable;

/**
 * Abstract class to represent a Grocery/Recipe List.
 * Both hold a list of Ingredients with a bridge table to represent the relationship between IngredientHolder and Ingredients.
 */
public abstract class IngredientHolder implements Parcelable {

    public static final String INGREDIENT_HOLDER = "ingredient_holder";

    String name;
    long id;

    /**
     * Check if the name of the Grocery is valid. Valid if it contains at least one non-empty character.
     * @param name          The name of the Grocery to check
     * @return              True if Grocery name is valid.
     */
    public static boolean isValidName(String name) {
        StringBuilder nameText = new StringBuilder();
        int numEmptySpaces = 0;
        nameText.append(name);
        for (int i = 0; i < nameText.length(); i++) {
            if (nameText.charAt(i) == ' ') numEmptySpaces++;
        }
        return numEmptySpaces < nameText.length();
    }

    public abstract boolean isGrocery();


    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
