package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;

public class IngredientEditPresenterImpl implements IngredientEditPresenter {

    private IngredientEditInteractor ingredientEditInteractor;
    private IngredientEditView view;

    public IngredientEditPresenterImpl(IngredientEditInteractor ingredientEditInteractor) {
        this.ingredientEditInteractor = ingredientEditInteractor;
    }

    @Override
    public void setView(IngredientEditView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void updateIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        ingredientEditInteractor.updateIngredient(ingredientHolder, ingredient);
    }

    @Override
    public void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient) {
        ingredientEditInteractor.deleteIngredient(ingredientHolder, ingredient);
    }

    @Override
    public boolean isNewIngredient(Ingredient ingredient) {
        return ingredientEditInteractor.isNewIngredient(ingredient);
    }

    @Override
    public void deleteRelationship(IngredientHolder ingredientHolder, Ingredient ingredient) {
        ingredientEditInteractor.deleteRelationship(ingredientHolder, ingredient);
    }
}
