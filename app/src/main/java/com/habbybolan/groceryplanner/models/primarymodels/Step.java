package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.habbybolan.groceryplanner.database.entities.StepsEntity;

public class Step implements Parcelable {

    public static final String STEP = "step";

    private long stepsId;
    private long recipeId;
    private String description;
    private int stepNumber;

    public Step(long stepsId, long recipeId, String description, int stepNumber) {
        this.stepsId = stepsId;
        this.recipeId = recipeId;
        this.description = description;
        this.stepNumber = stepNumber;
    }

    /**
     * Dummy Constructor used for creating a new Step in the database with description.
     * @param description   The step description
     * @param stepNumber    The numbering of the step
     */
    public Step(long recipeId, String description, int stepNumber) {
        this.recipeId = recipeId;
        this.description = description;
        this.stepNumber = stepNumber;
    }

    public Step(StepsEntity stepsEntity) {
        stepsId = stepsEntity.getStepsId();
        recipeId = stepsEntity.getRecipeId();
        description = stepsEntity.getDescription();
        stepNumber = stepsEntity.getStepNumber();
    }

    public Step(Parcel in) {
        stepsId = in.readLong();
        recipeId = in.readLong();
        description = in.readString();
        stepNumber = in.readInt();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public long getStepsId() {
        return stepsId;
    }
    public long getRecipeId() {
        return recipeId;
    }
    public String getDescription() {
        return description;
    }
    public int getStepNumber() {
        return stepNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(stepsId);
        dest.writeLong(recipeId);
        dest.writeString(description);
        dest.writeInt(stepNumber);
    }
}
