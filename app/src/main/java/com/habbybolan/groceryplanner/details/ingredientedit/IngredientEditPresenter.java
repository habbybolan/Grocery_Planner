package com.habbybolan.groceryplanner.details.ingredientedit;

import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;

public interface IngredientEditPresenter {

    void setView(IngredientEditView view);
    void destroy();

    void updateIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);

    void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    boolean isNewIngredient(Ingredient ingredient);
}
