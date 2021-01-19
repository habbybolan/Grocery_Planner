package com.habbybolan.groceryplanner.details.recipesteps;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

import java.util.concurrent.ExecutionException;

public class RecipeStepPresenterImpl implements RecipeStepPresenter {

    private RecipeStepInteractor recipeStepInteractor;
    private RecipeStepView view;

    public RecipeStepPresenterImpl(RecipeStepInteractor recipeStepInteractor) {
        this.recipeStepInteractor = recipeStepInteractor;
    }

    @Override
    public void setView(RecipeStepView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void createStepList(Recipe recipe) {
        try {
            if (isViewAttached())
                view.showStepList(recipeStepInteractor.fetchSteps(recipe));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to retrieve steps");
        }
    }

    @Override
    public void addNewStep(Recipe recipe, Step step) {
        recipeStepInteractor.addNewStep(step);
        /*try {
            if (isViewAttached())
                view.showStepList(recipeStepInteractor.fetchSteps(recipe));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("failed to retrieve steps");
        }*/
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeStepInteractor.deleteRecipe(recipe);
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
