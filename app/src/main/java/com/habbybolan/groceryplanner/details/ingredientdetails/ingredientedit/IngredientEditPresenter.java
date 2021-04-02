package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

public interface IngredientEditPresenter {

    void setView(IngredientEditView view);
    void destroy();

    void updateIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);

    void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    boolean isNewIngredient(Ingredient ingredient);
    void deleteRelationship(IngredientHolder ingredientHolder, Ingredient ingredient);
}
