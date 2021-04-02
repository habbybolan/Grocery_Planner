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

    public FoodType(String type) {
        this.type = type;
        imageResource = getImageResource(type);
    }

    private int getImageResource(String type) {
        switch (type) {
            case FRUITS:
                id = 0;
                return R.drawable.fruits;
            case VEGETABLES:
                id = 1;
                return R.drawable.vegeatables;
            case GRAINS:
                id = 2;
                return R.drawable.grains;
            case PROTEIN:
                id = 3;
                return R.drawable.protein;
            case DAIRY:
                id = 4;
                return R.drawable.dairy;
            case OILS:
                id = 5;
                return R.drawable.oils;
            default:
                id = 6;
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
