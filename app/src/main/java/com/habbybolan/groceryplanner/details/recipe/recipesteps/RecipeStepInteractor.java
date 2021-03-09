package com.habbybolan.groceryplanner.details.recipe.recipesteps;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipeStepInteractor {

    /**
     * Get all Step objects associated with Recipe from the database.
     * @param recipe    The list of steps to display
     * @return          All Steps associated with recipe
     */
    List<Step> fetchSteps(Recipe recipe) throws ExecutionException, InterruptedException;

    void addNewStep(Step step);
    void deleteRecipe(Recipe recipe);
}
