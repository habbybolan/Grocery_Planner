package com.habbybolan.groceryplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habbybolan.groceryplanner.models.Step;

@Entity
public class StepsEntity {

    @PrimaryKey(autoGenerate = true)
    private long stepsId;
    private long recipeId;
    private String description;
    @ColumnInfo(name = "step_number")
    private int stepNumber;

    public StepsEntity(long stepsId, long recipeId, String description, int stepNumber) {
        this.stepsId = stepsId;
        this.recipeId = recipeId;
        this.description = description;
        this.stepNumber = stepNumber;
    }


    public StepsEntity(Step step) {
        stepsId = step.getStepsId();
        recipeId = step.getRecipeId();
        description = step.getDescription();
        stepNumber = step.getStepNumber();
    }

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
}
