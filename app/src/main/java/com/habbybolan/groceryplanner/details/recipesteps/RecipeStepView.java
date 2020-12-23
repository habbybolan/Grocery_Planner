package com.habbybolan.groceryplanner.details.recipesteps;

import com.habbybolan.groceryplanner.models.Step;

import java.util.List;

public interface RecipeStepView {

    void showStepList(List<Step> steps);
    void loadingStarted();
    void loadingFailed(String message);
}
