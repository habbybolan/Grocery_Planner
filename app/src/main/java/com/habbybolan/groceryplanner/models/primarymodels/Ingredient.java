package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;

import java.util.Objects;

public class Ingredient implements Parcelable {

    protected long id;
    protected float price;
    protected int pricePer;
    protected String priceType;
    protected float quantity;
    protected Long quantityMeasId;
    @NonNull
    protected String name = "";
    protected FoodType foodType;

    public final static String INGREDIENT = "ingredient";

    public Ingredient(IngredientEntity ingredientEntity, float quantity, long quantityMeasId) {
        id = ingredientEntity.getIngredientId();
        name = ingredientEntity.getIngredientName();
        price = ingredientEntity.getPrice();
        pricePer = ingredientEntity.getPricePer();
        priceType = ingredientEntity.getPriceType();
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
    }

    public Ingredient(@NonNull long id, @NonNull String name, float price, int pricePer, String priceType,
                      float quantity, Long quantityMeasId, FoodType foodType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pricePer = pricePer;
        this.priceType = priceType;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
        this.foodType = foodType;
    }

    public Ingredient(Ingredient ingredient) {
        this(ingredient.getId(), ingredient.getName(), ingredient.getPrice(), ingredient.getPricePer(),
                ingredient.getPriceType(), ingredient.getQuantity(), ingredient.getQuantityMeasId(), ingredient.getFoodType());
    }

    /**
     * Builder for Ingredient.
     */
    public static class IngredientBuilder {

        private long id;
        private float price;
        private int pricePer;
        private String priceType;
        private float quantity;
        private Long quantityMeasId;
        @NonNull
        private final String name;
        private FoodType foodType;

        // for creating an ingredient from the Room database
        public IngredientBuilder(long id, @NonNull String name) {
            this.name = name;
            this.id = id;
        }

        // for creating an ingredient from the web service
        public IngredientBuilder(@NonNull String name) {
            this.name = name;
        }

        public IngredientBuilder setPrice(float price) {
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
        public IngredientBuilder setQuantity(float quantity) {
            this.quantity = quantity;
            return this;
        }
        public IngredientBuilder setQuantityMeasId(Long quantityMeasId) {
            this.quantityMeasId = quantityMeasId;
            return this;
        }
        public IngredientBuilder setFoodType(String foodType) {
            this.foodType = new FoodType(foodType);
            return this;
        }

        public Ingredient build() {
            Ingredient ingredient = new Ingredient();
            ingredient.id = id;
            ingredient.name = name;
            ingredient.price = price;
            ingredient.priceType = priceType;
            ingredient.pricePer = pricePer;
            ingredient.quantity = quantity;
            ingredient.quantityMeasId = quantityMeasId;
            if (foodType == null) foodType = new FoodType(FoodType.OTHER);
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
        price = in.readFloat();
        pricePer = in.readInt();
        priceType = in.readString();
        quantity = in.readFloat();
        quantityMeasId = in.readLong();
        foodType = new FoodType(in.readString());
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
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeInt(pricePer);
        dest.writeString(priceType);
        dest.writeFloat(quantity);
        dest.writeLong(quantityMeasId);
        dest.writeString(foodType.getType());
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
    public boolean hasQuantityMeasId() {
        return quantityMeasId != null;
    }

    public String getName() {
        return name;
    }
    public float getPrice() {
        return price;
    }
    public int getPricePer() {
        return pricePer;
    }
    public String getPriceType() {
        return priceType;
    }
    public float getQuantity() {
        return quantity;
    }
    public Long getQuantityMeasId() {
        return quantityMeasId;
    }
    public FoodType getFoodType() {
        return foodType;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setPricePer(int pricePer) {
        this.pricePer = pricePer;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
    public void setQuantityMeasId(long quantityMeasId) {
        this.quantityMeasId = quantityMeasId;
    }
    public void setFoodType(String foodType) {
        this.foodType = new FoodType(foodType);
    }
}
