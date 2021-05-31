package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.habbybolan.groceryplanner.models.databasetuples.RecipeIngredientsTuple;
import com.habbybolan.groceryplanner.models.databasetuples.RecipeTagTuple;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class Recipe extends IngredientHolder implements Parcelable {

    protected boolean isFavorite;
    protected String description;
    protected int prepTime = -1;
    protected int cookTime = -1;
    protected int servingSize = -1;
    protected Nutrition calories;
    protected Nutrition fat;
    protected Nutrition saturatedFat;
    protected Nutrition carbohydrates;
    protected Nutrition fiber;
    protected Nutrition sugar;
    protected Nutrition protein;
    protected String instructions;
    protected Timestamp dateCreated;
    protected int likes;

    protected List<RecipeTag> recipeTags = new ArrayList<>();
    protected List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {}

    public Recipe(Parcel in) {
        super(in);
        name = in.readString();
        isFavorite = in.readByte() != 0;
        description = in.readString();
        prepTime = in.readInt();
        cookTime = in.readInt();
        servingSize = in.readInt();
        calories = in.readParcelable(Nutrition.class.getClassLoader());
        fat = in.readParcelable(Nutrition.class.getClassLoader());
        saturatedFat = in.readParcelable(Nutrition.class.getClassLoader());
        carbohydrates = in.readParcelable(Nutrition.class.getClassLoader());
        fiber = in.readParcelable(Nutrition.class.getClassLoader());
        sugar = in.readParcelable(Nutrition.class.getClassLoader());
        protein = in.readParcelable(Nutrition.class.getClassLoader());
        instructions = in.readString();
        likes = in.readInt();
        dateCreated = (Timestamp) in.readSerializable();
        in.readList(recipeTags, RecipeTag.class.getClassLoader());
        in.readList(ingredients, Ingredient.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(description);
        dest.writeInt(prepTime);
        dest.writeInt(cookTime);
        dest.writeInt(servingSize);
        dest.writeParcelable(calories, flags);
        dest.writeParcelable(fat, flags);
        dest.writeParcelable(saturatedFat, flags);
        dest.writeParcelable(carbohydrates, flags);
        dest.writeParcelable(fiber, flags);
        dest.writeParcelable(sugar, flags);
        dest.writeParcelable(protein, flags);
        dest.writeString(instructions);
        dest.writeInt(likes);
        dest.writeSerializable(dateCreated);
        dest.writeList(recipeTags);
        dest.writeList(ingredients);
    }

    public interface RecipeBuilderInterface<T> {
        T setDescription(String description);
        T setPrepTime(int prepTime);
        T setCookTime(int cookTime);
        T setServingSize(int servingSize);
        T setCalories(Nutrition calories);
        T setFat(Nutrition fat);
        T setSaturatedFat(Nutrition saturatedFat);
        T setCarbohydrates(Nutrition carbohydrates);
        T setFiber(Nutrition fiber);
        T setSugar(Nutrition sugar);
        T setProtein(Nutrition protein);
        T setInstructions(String instructions);
        T setDateCreated(Timestamp dateCreated);
        T setLikes(int likes);
        T setIngredients(List<Ingredient> ingredients);
        T setRecipeTags(List<RecipeTag> recipeTags);
    }

    /**
     * Given a list of nutritional facts, update the recipe with those facts
     * @param nutritionList The list of nutritional facts
     */
    public void updateNutrition(List<Nutrition> nutritionList) {
        for (Nutrition nutrition : nutritionList) {
            switch(nutrition.getName()) {
                case Nutrition.CALORIES:
                    calories = nutrition;
                    break;
                case Nutrition.FAT:
                    fat = nutrition;
                    break;
                case Nutrition.SATURATED_FAT:
                    saturatedFat = nutrition;
                    break;
                case Nutrition.CARBOHYDRATES:
                    carbohydrates = nutrition;
                    break;
                case Nutrition.FIBRE:
                    fiber = nutrition;
                    break;
                case Nutrition.SUGAR:
                    sugar = nutrition;
                    break;
                case Nutrition.PROTEIN:
                    protein = nutrition;
                    break;
            }
        }
    }

    /**
     * Takes a nutrition object and set as the associated recipe nutrition object
     * @param nutrition Nutrition object to apply to recipe.
     */
    public void findNutrition(Nutrition nutrition) {
        switch (nutrition.getName()) {
            case Nutrition.CALORIES:
                calories = nutrition;
                break;
            case Nutrition.FAT:
                fat = nutrition;
                break;
            case Nutrition.SATURATED_FAT:
                saturatedFat = nutrition;
                break;
            case Nutrition.FIBRE:
                fiber = nutrition;
                break;
            case Nutrition.PROTEIN:
                protein = nutrition;
                break;
            case Nutrition.SUGAR:
                sugar = nutrition;
                break;
            case Nutrition.CARBOHYDRATES:
                carbohydrates = nutrition;
                break;
            default:
                throw new IllegalArgumentException(nutrition.getName() + " doesn't exists as a nutrition.");
        }
    }

    public static List<RecipeTag> convertToRecipeTag(List<RecipeTagTuple> tuples) {
        List<RecipeTag> recipeTags = new ArrayList<>();
        for (RecipeTagTuple tuple : tuples) recipeTags.add(new RecipeTag(tuple.tagId, tuple.onlineTagId, tuple.title, tuple.dateUpdated, tuple.dateSynchronized));
        return recipeTags;
    }

    public static List<Ingredient> convertTuplesToIngredients(List<RecipeIngredientsTuple> ingredientsTuple) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (RecipeIngredientsTuple tuple : ingredientsTuple) {
            ingredients.add(new Ingredient.IngredientBuilder(tuple.ingredientName).setFoodType(tuple.foodTypeId).setQuantity(tuple.quantity).setQuantityMeasId(tuple.quantityMeasId).build());
        }
        return ingredients;
    }

    public List<Nutrition> getNutritionList() {
        // create a list oif the Nutrition, in order of their ids
        List<Nutrition> nutritionList = new ArrayList<>();
        nutritionList.add(calories != null ? calories : new Nutrition(Nutrition.CALORIES, 0, null));
        nutritionList.add(fat != null ? fat : new Nutrition(Nutrition.FAT, 0, null));
        nutritionList.add(saturatedFat != null ? saturatedFat : new Nutrition(Nutrition.SATURATED_FAT, 0, null));
        nutritionList.add(carbohydrates != null ? carbohydrates : new Nutrition(Nutrition.CARBOHYDRATES, 0, null));
        nutritionList.add(fiber != null ? fiber : new Nutrition(Nutrition.FIBRE, 0, null));
        nutritionList.add(sugar != null ? sugar : new Nutrition(Nutrition.SUGAR, 0, null));
        nutritionList.add(protein != null ? protein : new Nutrition(Nutrition.PROTEIN, 0, null));
        return nutritionList;
    }

    public void removeNutrition(Nutrition nutrition) {
        switch (nutrition.getName()) {
            case Nutrition.CALORIES:
                calories = null;
                break;
            case Nutrition.FAT:
                fat = null;
                break;
            case Nutrition.SATURATED_FAT:
                saturatedFat = null;
                break;
            case Nutrition.FIBRE:
                fiber = null;
                break;
            case Nutrition.PROTEIN:
                protein = null;
                break;
            case Nutrition.SUGAR:
                sugar = null;
                break;
            case Nutrition.CARBOHYDRATES:
                carbohydrates = null;
                break;
            default:
                throw new IllegalArgumentException(nutrition.getName() + " doesn't exists as a nutrition.");
        }
    }

    public void setRecipeTags(List<RecipeTag> recipeTags) {
        this.recipeTags = recipeTags;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }
    public int getPrepTime() {
        return prepTime;
    }
    public int getCookTime() {
        return cookTime;
    }
    public int getServingSize() {
        return servingSize;
    }
    public Nutrition getCalories() {
        return calories;
    }
    public Nutrition getFat() {
        return fat;
    }
    public Nutrition getSaturatedFat() {
        return saturatedFat;
    }
    public Nutrition getCarbohydrates() {
        return carbohydrates;
    }
    public Nutrition getFiber() {
        return fiber;
    }
    public Nutrition getSugar() {
        return sugar;
    }
    public Nutrition getProtein() {
        return protein;
    }
    public String getInstructions() {
        return instructions;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public Timestamp getDateCreated() {
        return dateCreated;
    }
    public int getLikes() {
        return likes;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }
    public void setCalories(Nutrition calories) {
        this.calories = calories;
    }
    public void setFat(Nutrition fat) {
        this.fat = fat;
    }
    public void setSaturatedFat(Nutrition saturatedFat) {
        this.saturatedFat = saturatedFat;
    }
    public void setCarbohydrates(Nutrition carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    public void setFiber(Nutrition fiber) {
        this.fiber = fiber;
    }
    public void setSugar(Nutrition sugar) {
        this.sugar = sugar;
    }
    public void setProtein(Nutrition protein) {
        this.protein = protein;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
