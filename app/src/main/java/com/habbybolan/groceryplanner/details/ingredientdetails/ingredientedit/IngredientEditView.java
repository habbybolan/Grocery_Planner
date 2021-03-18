package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

public interface IngredientEditView {

    /**
     * Creates a popup of all Ingredient Items created inside database to be added
     */
    void showListOfIngredients(String[] ingredientNames);

    void loadingStarted();
    void loadingFailed(String message);
}
