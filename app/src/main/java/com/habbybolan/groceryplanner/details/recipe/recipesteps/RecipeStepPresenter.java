package com.habbybolan.groceryplanner.details.recipe.recipesteps;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

public interface RecipeStepPresenter {

    void setView(RecipeStepView view);
    void destroy();

    void createStepList(Recipe recipe);
    void addNewStep(Recipe recipe, Step step);
    void deleteRecipe(Recipe recipe);

}
