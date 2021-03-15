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
    private FoodType foodType;

    public final static String INGREDIENT = "ingredient";
    public final static String INGREDIENT_CHECKED = "ingredient_checked";
    public final static String INGREDIENT_UNCHECKED = "ingredient_unchecked";


    public Ingredient(IngredientEntity ingredientEntity, int quantity, String quantityType) {
        name = ingredientEntity.getIngredientName();
        price = ingredientEntity.getPrice();
        pricePer = ingredientEntity.getPricePer();
        priceType = ingredientEntity.getPriceType();
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public Ingredient(@NonNull String name, int price, int pricePer, String priceType, int quantity, String quantityType, FoodType foodType) {
        this.name = name;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.foodType = foodType;
    }

    public Ingredient(Ingredient ingredient) {
        this(ingredient.getName(), ingredient.getPrice(), ingredient.getPricePer(), ingredient.getPriceType(), ingredient.getQuantity(), ingredient.getQuantityType(), ingredient.getFoodType());
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
        private FoodType foodType;

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
        public IngredientBuilder setFoodType(String foodType) {
            this.foodType = new FoodType(foodType);
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
            ingredient.foodType = foodType;
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
    public FoodType getFoodType() {
        return foodType;
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
    public void setFoodType(String foodType) {
        this.foodType = new FoodType(foodType);
    }
}
