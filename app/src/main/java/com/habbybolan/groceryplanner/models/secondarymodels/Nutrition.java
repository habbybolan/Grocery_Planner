package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.models.SyncJSON;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineModel;

import java.sql.Timestamp;

/**
 * Models Nutritional values of Recipes and Ingredients.
 */
public class Nutrition extends OnlineModel implements SyncJSON {

    public static final String CALORIES = "Calories";
    public static final String FAT = "fat";
    public static final String SATURATED_FAT = "Saturated Fat";
    public static final String CARBOHYDRATES = "Carbohydrates";
    public static final String FIBRE = "fibre";
    public static final String SUGAR = "Sugar";
    public static final String PROTEIN = "Protein";

    public static final long CALORIES_ID = 1;
    public static final long FAT_ID = 2;
    public static final long SATURATED_FAT_ID = 3;
    public static final long CARBOHYDRATES_ID = 4;
    public static final long FIBER_ID = 5;
    public static final long SUGAR_ID = 6;
    public static final long PROTEIN_ID = 7;

    private String name;
    // Amount of the nutrition value
    private int amount;
    private MeasurementType measurementType;

    // True if the nutrition is being added to the recipe, false if being deleted
    private boolean isAddedToRecipe = true;

    private Nutrition(String name, int amount, @MeasurementType.measurementIds Long measurementId, Timestamp dateUpdated, Timestamp dateSynchronized) {
        measurementType = new MeasurementType(measurementId);
        this.name = name;
        this.amount = amount;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    public Nutrition(String name, int amount, @MeasurementType.measurementIds Long measurementId) {
        measurementType = new MeasurementType(measurementId);
        this.name = name;
        this.amount = amount;
    }

    public Nutrition(String name, int amount, @MeasurementType.measurementIds Long measurementId, boolean isAddedToRecipe, Timestamp dateUpdated, Timestamp dateSynchronized) {
        measurementType = new MeasurementType(measurementId);
        this.name = name;
        this.amount = amount;
        this.isAddedToRecipe = isAddedToRecipe;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    public Nutrition(long nutritionId, int amount, @MeasurementType.measurementIds Long measurementId, Timestamp dateUpdated, Timestamp dateSynchronized) {
        measurementType = new MeasurementType(measurementId);
        this.amount = amount;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
        switch ((int) nutritionId) {
            case (int) CALORIES_ID:
                name = CALORIES;
                break;
            case (int) FAT_ID:
                name = FAT;
                break;
            case (int) SATURATED_FAT_ID:
                name = SATURATED_FAT;
                break;
            case (int) CARBOHYDRATES_ID:
                name = CARBOHYDRATES;
                break;
            case (int) FIBER_ID:
                name = FIBRE;
                break;
            case (int) SUGAR_ID:
                name = SUGAR;
                break;
            case (int) PROTEIN_ID:
                name = PROTEIN;
                break;
            default:
                throw new IllegalArgumentException(nutritionId + " is not a valid nutrition id");
        }
    }

    public static class NutritionBuilder {

        private String name;
        private int amount;
        private Long measurementType;
        private Timestamp dateSynchronized;
        private Timestamp dateUpdated;

        public NutritionBuilder(String name, int amount, @MeasurementType.measurementIds Long measurementType) {
            this.name = name;
            this.amount = amount;
            this.measurementType = measurementType;
        }
        public NutritionBuilder setDateSynchronized(Timestamp dateSynchronized) {
            this.dateSynchronized = dateSynchronized;
            return this;
        }
        public NutritionBuilder setDateUpdated(Timestamp dateUpdated) {
            this.dateUpdated = dateUpdated;
            return this;
        }
        public Nutrition build() {
            Nutrition nutrition = new Nutrition(name, amount, measurementType);
            nutrition.setDateSynchronized(dateSynchronized);
            nutrition.setDateUpdated(dateUpdated);
            return nutrition;
        }

    }

    protected Nutrition(Parcel in) {
        super(in);
        name = in.readString();
        amount = in.readInt();
        dateSynchronized = (Timestamp) in.readSerializable();
        dateUpdated = (Timestamp) in.readSerializable();
    }

    public static final Creator<Nutrition> CREATOR = new Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };

    @Override
    public Nutrition clone() {
        return new Nutrition(name, amount, measurementType.getMeasurementId(), dateUpdated, dateSynchronized);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeInt(amount);
        dest.writeSerializable(dateSynchronized);
        dest.writeSerializable(dateUpdated);
    }

    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public boolean getIsAddedToRecipe() {
        return isAddedToRecipe;
    }
    public MeasurementType getMeasurementType() {
        return measurementType;
    }
    public void setMeasurement(Long measurementId) {
        measurementType = new MeasurementType(measurementId);
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setIsAddedToRecipe(boolean isAddedToRecipe) {
        this.isAddedToRecipe = isAddedToRecipe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static String getNameFromId(long id) {
        switch ((int) id) {
            case 1:
                return CALORIES;
            case 2:
                return FAT;
            case 3:
                return SATURATED_FAT;
            case 4:
                return CARBOHYDRATES;
            case 5:
                return FIBRE;
            case 6:
                return SUGAR;
            default: return PROTEIN;
        }
    }

    public static long getIdFromName(String name) {
        switch (name) {
            case CALORIES:
                return CALORIES_ID;
            case FAT:
                return FAT_ID;
            case SATURATED_FAT:
                return SATURATED_FAT_ID;
            case CARBOHYDRATES:
                return CARBOHYDRATES_ID;
            case FIBRE:
                return FIBER_ID;
            case SUGAR:
                return SUGAR_ID;
            default: return PROTEIN_ID;
        }
    }

    @Override
    public JsonObject createSyncJSON() {
        return SyncJSON.getIsUpdate(dateUpdated, dateSynchronized) ? createSyncJSONUpdate() : createSyncJSONSync();
    }

    @Override
    public JsonObject createSyncJSONUpdate() {
        JsonObject json = new JsonObject();
        json.addProperty(SyncJSON.UpdateIdentifier, OfflineUpdateIdentifier.UPDATE.toString());
        json.addProperty("id", getIdFromName(name));
        json.addProperty("name", name);
        json.addProperty("amount", amount);
        json.addProperty("measurement_id", measurementType.getMeasurementId());
        if (dateUpdated != null) json.addProperty("date_updated", dateUpdated.toString());
        if (dateSynchronized != null) json.addProperty("date_synchronized", dateSynchronized.toString());
        return json;
    }

    @Override
    public JsonObject createSyncJSONSync() {
        JsonObject json = new JsonObject();
        json.addProperty(SyncJSON.UpdateIdentifier, OfflineUpdateIdentifier.SYNC.toString());
        json.addProperty("id", getIdFromName(name));
        json.addProperty("name", name);
        if (dateUpdated != null) json.addProperty("date_updated", dateUpdated.toString());
        if (dateSynchronized != null) json.addProperty("date_synchronized", dateSynchronized.toString());
        return json;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Nutrition nutrition = (Nutrition) obj;
        return nutrition.name.equals(name) &&
                nutrition.amount == amount &&
                nutrition.measurementType.equals(measurementType);
    }
}
