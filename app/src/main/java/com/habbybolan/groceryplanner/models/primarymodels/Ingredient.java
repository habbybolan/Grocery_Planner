package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.models.secondarymodels.FoodType;

import java.util.Objects;

public class Ingredient extends OnlineModel {

    protected long id;
    protected float quantity;
    protected Long quantityMeasId;
    @NonNull
    protected String name = "";
    protected FoodType foodType;

    public final static String INGREDIENT = "ingredient";

    public Ingredient(@NonNull long id, Long onlineId, @NonNull String name,
                      float quantity, Long quantityMeasId, FoodType foodType) {
        this.id = id;
        this.onlineId = onlineId;
        this.name = name;
        this.quantity = quantity;
        this.quantityMeasId = quantityMeasId;
        this.foodType = foodType;
    }

    public Ingredient(Ingredient ingredient) {
        this(ingredient.getId(), ingredient.getOnlineId(), ingredient.getName(), ingredient.getQuantity(), ingredient.getQuantityMeasId(), ingredient.getFoodType());
    }

    /**
     * Builder for Ingredient.
     */
    public static class IngredientBuilder {

        private long id;
        private Long onlineId;
        private float quantity;
        private Long quantityMeasId;
        @NonNull
        private final String name;
        private FoodType foodType;

        // for creating an ingredient from the Room database
        public IngredientBuilder(long id, Long onlineId, @NonNull String name) {
            this.name = name;
            this.id = id;
            this.onlineId = onlineId;
        }

        // for creating an ingredient from the web service
        public IngredientBuilder(@NonNull String name) {
            this.name = name;
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
            ingredient.onlineId = onlineId;
            ingredient.name = name;
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
        super(in);
        id = in.readLong();
        name = Objects.requireNonNull(in.readString());
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
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeString(name);
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

    public boolean hasQuantity() {
        return quantity > 0;
    }
    public boolean hasQuantityMeasId() {
        return quantityMeasId != null;
    }

    public String getName() {
        return name;
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
