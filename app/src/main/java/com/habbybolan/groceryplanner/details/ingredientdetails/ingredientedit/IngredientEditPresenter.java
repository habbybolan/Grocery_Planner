package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

public interface IngredientEditPresenter {

    void setView(IngredientEditView view);
    void destroy();

    void updateIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);

    void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
    boolean isNewIngredient(Ingredient ingredient);
    void deleteRelationship(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
}
