package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

import javax.inject.Inject;

public class IngredientEditPresenterImpl implements IngredientEditContract.Presenter {

    private IngredientEditContract.Interactor interactor;
    private IngredientEditContract.IngredientEditView view;

    @Inject
    public IngredientEditPresenterImpl(IngredientEditContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(IngredientEditContract.IngredientEditView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient) {
        interactor.deleteIngredient(ingredientHolder, ingredient);
    }

    @Override
    public void updateIngredient(OfflineIngredientHolder ingredientHolder, String name, String quantity, String quantityType, String foodType, long ingredientId) {
        if (interactor.updateIngredient(ingredientHolder, name, quantity, quantityType, foodType, ingredientId))
            view.saveSuccessful();
         else
             view.saveFailed("Invalid Ingredient");
    }
}
