package com.habbybolan.groceryplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

import java.util.Objects;

public class Ingredient implements Parcelable {

    private int price;
    private int pricePer;
    private String priceType;
    private int quantity;
    private String quantityType;
    @NonNull
    private String name = "";
    private Section section;

    public final static String INGREDIENT = "ingredient";


    public Ingredient(IngredientEntity ingredientEntity, int quantity, String quantityType) {
        name = ingredientEntity.getIngredientName();
        price = ingredientEntity.getPrice();
        pricePer = ingredientEntity.getPricePer();
        priceType = ingredientEntity.getPriceType();
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    /**
     * Builder for Ingredient.
     */
    public static class IngredientBuilder {

        private int price;
        private int pricePer;
        private String priceType;
        private int quantity;
        private String quantityType;
        @NonNull
        private String name;
        private Section section;

        public IngredientBuilder(@NonNull String name) {
            this.name = name;
        }

        public IngredientBuilder setPrice(int price) {
            this.price = price;
            return this;
        }
        public IngredientBuilder setPricePer(int pricePer) {
            this.pricePer = pricePer;
            return this;
        }
        public IngredientBuilder setPriceType(String priceType) {
            this.priceType = priceType;
            return this;
        }
        public IngredientBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }
        public IngredientBuilder setQuantityType(String quantityType) {
            this.quantityType = quantityType;
            return this;
        }
        public IngredientBuilder setSection(Section section) {
            this.section = section;
            return this;
        }

        public Ingredient build() {
            Ingredient ingredient = new Ingredient();
            ingredient.name = name;
            ingredient.price = price;
            ingredient.priceType = priceType;
            ingredient.pricePer = pricePer;
            ingredient.quantity = quantity;
            ingredient.quantityType = quantityType;
            ingredient.section = section;
            return ingredient;
        }
    }

    /**
     * Create an empty 'dummy' ingredient. Used for adding new Ingredients.
     */
    public Ingredient() {}


    protected Ingredient(Parcel in) {
        name = Objects.requireNonNull(in.readString());
        price = in.readInt();
        pricePer = in.readInt();
        priceType = in.readString();
        quantity = in.readInt();
        quantityType = in.readString();
    }

    /**
     * Find if the Ingredient is an empty ingredient with none of its values set.
     * @return  True if it's an empty Ingredient.
     */
    public static boolean isEmptyIngredient(Ingredient ingredient) {
        return ingredient.getName().equals("");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeInt(pricePer);
        dest.writeString(priceType);
        dest.writeInt(quantity);
        dest.writeString(quantityType);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * Check if the Manual values set by user creates a valid Ingredient
     * @param name      name of the Ingredient.
     * @param price     price of the Ingredient.
     * @param pricePer  PricePer of the Ingredient.
     * @param quantity  quantity of the Ingredient.
     * @return          True if the user created Ingredient is valid.
     */
    public static boolean isValidIngredient(String name, String price, String pricePer, String quantity) {
        return isValidName(name)
                && isValidIntegerText(price)
                && isValidIntegerText(pricePer)
                && isValidIntegerText(quantity);
    }

    /**
     * Check if the name of the Ingredient is valid. Valid if it contains at least one non-empty character.
     * @param name    The name of the Ingredient to check
     * @return              True if Ingredient name is valid.
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

    /**
     * Check if the Integer values of the Ingredient is valid. Valid if it only contains integers.
     * @param integer   The String version of the Integer value to check if it's a valid Integer.
     * @return          True if Ingredient has valid Integer value.
     */
    public static boolean isValidIntegerText(String integer) {
        StringBuilder integerText = new StringBuilder();
        integerText.append(integer);
        for (int i = 0; i < integerText.length(); i++) {
            // check if the character contains a non-digit value, returning false if it does
            if (!Character.isDigit(integerText.charAt(i)))
                return false;
        }
        // all values are numbers, return true
        return true;
    }

    public boolean hasPrice() {
        return price > 0;
    }
    public boolean hasPricePer() {
        return pricePer > 0;
    }
    public boolean hasPriceType() {
        return priceType != null;
    }
    public boolean hasQuantity() {
        return quantity > 0;
    }
    public boolean hasQuantityType() {
        return quantityType != null;
    }


    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getPricePer() {
        return pricePer;
    }
    public String getPriceType() {
        return priceType;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getQuantityType() {
        return quantityType;
    }
    public Section getSection() {
        return section;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setPricePer(int pricePer) {
        this.pricePer = pricePer;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
