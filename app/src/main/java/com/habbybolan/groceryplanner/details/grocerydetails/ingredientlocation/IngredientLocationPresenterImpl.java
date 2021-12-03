package com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation;

import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import javax.inject.Inject;

public class IngredientLocationPresenterImpl implements IngredientLocationPresenter {

    private IngredientLocationInteractor interactor;

    @Inject
    public IngredientLocationPresenterImpl(IngredientLocationInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void deleteDirectRelationship(Grocery grocery, GroceryIngredient ingredient) {
        interactor.deleteDirectRelationship(grocery, ingredient);
    }

    @Override
    public void deleteRecipeRelationship(Grocery grocery, GroceryIngredient ingredient, int position) {
        interactor.deleteRecipeRelationship(grocery, ingredient, position);
    }
}
