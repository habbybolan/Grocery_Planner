package com.habbybolan.groceryplanner.details.recipe.recipesteps;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeStepInteractorImpl implements RecipeStepInteractor {

    private DatabaseAccess databaseAccess;

    public RecipeStepInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public List<Step> fetchSteps(Recipe recipe) throws ExecutionException, InterruptedException {
        return databaseAccess.fetchStepsFromRecipe(recipe.getId());
    }

    @Override
    public void addNewStep(Step step) {
        databaseAccess.addStep(step);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        databaseAccess.deleteRecipe(recipe);
    }
}
