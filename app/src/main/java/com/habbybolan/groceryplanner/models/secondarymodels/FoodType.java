package com.habbybolan.groceryplanner.models.secondarymodels;

import com.habbybolan.groceryplanner.R;

import java.util.ArrayList;
import java.util.List;

public class FoodType {

    // The type of food category
    private String type;
    // The drawable resource that's associated with the type
    private int imageResource;
    // Unique id used in the RecyclerView
    private long id;

    public static final String FRUITS = "fruits";
    public static final String VEGETABLES = "vegetables";
    public static final String DAIRY = "dairy";
    public static final String PROTEIN = "protein";
    public static final String GRAINS = "grains";
    public static final String OILS = "oils";
    public static final String OTHER = "other";

    public static final int FRUITS_ID = 0;
    public static final int VEGETABLES_ID = 1;
    public static final int DAIRY_ID = 2;
    public static final int PROTEIN_ID = 3;
    public static final int GRAINS_ID = 4;
    public static final int OILS_ID = 5;
    public static final int OTHER_ID = 6;

    public FoodType(String type) {
        this.type = type;
        imageResource = getImageResource(type);
    }
    public FoodType(long id) {
        this.type = getFoodTypeString(id);
        imageResource = getImageResource(type);
    }

    public String getFoodTypeString(long id) {
        switch ((int) id) {
            case FRUITS_ID:
                return FRUITS;
            case VEGETABLES_ID:
                return VEGETABLES;
            case GRAINS_ID:
                return GRAINS;
            case PROTEIN_ID:
                return PROTEIN;
            case DAIRY_ID:
                return DAIRY;
            case OILS_ID:
                return OILS;
            default:
                return OTHER;
        }
    }

    private int getImageResource(String type) {
        switch (type) {
            case FRUITS:
                id = FRUITS_ID;
                return R.drawable.fruits;
            case VEGETABLES:
                id = VEGETABLES_ID;
                return R.drawable.vegeatables;
            case GRAINS:
                id = GRAINS_ID;
                return R.drawable.grains;
            case PROTEIN:
                id = PROTEIN_ID;
                return R.drawable.protein;
            case DAIRY:
                id = DAIRY_ID;
                return R.drawable.dairy;
            case OILS:
                id = OILS_ID;
                return R.drawable.oils;
            default:
                id = OTHER_ID;
                return R.drawable.other;
        }
    }

    /**
     * Creates a list of all possible FoodTypes
     * @return  List of all possible FoodTypes
     */
    public static List<FoodType> getAllFoodTypes() {
        List<FoodType> foodTypes = new ArrayList<>();
        foodTypes.add(new FoodType(FRUITS));
        foodTypes.add(new FoodType(VEGETABLES));
        foodTypes.add(new FoodType(DAIRY));
        foodTypes.add(new FoodType(PROTEIN));
        foodTypes.add(new FoodType(GRAINS));
        foodTypes.add(new FoodType(OILS));
        foodTypes.add(new FoodType(OTHER));
        return foodTypes;
    }

    public int getImageResource() {
        return imageResource;
    }
    public String getType() {
        return type;
    }
    public long getId() {
        return id;
    }
}
