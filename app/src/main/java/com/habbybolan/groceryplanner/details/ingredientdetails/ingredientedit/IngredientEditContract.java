package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

public interface IngredientEditContract {

    interface Presenter {
        void setView(IngredientEditView view);
        void destroy();

        void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);

        void updateIngredient(OfflineIngredientHolder ingredientHolder, String name, String quantity, String quantityType, String foodType, long ingredientId);
    }

    interface Interactor {
        void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient);
        /**
         * Update/add the Ingredient to the database Ingredient table, depending on if the Ingredient id exists or not.
         * add the IngredientHolder-Ingredient relationship to the Bridge table if it doesn't already exist.
         * @param ingredientHolder      The IngredientHolder where the Ingredient is being added to.
         * @param name                  Name of Ingredient
         * @param quantity              String quantity of Ingredient
         * @param quantityType          String QuantityType of Ingredient
         */
        boolean updateIngredient(OfflineIngredientHolder ingredientHolder, String name, String quantity, String quantityType, String foodType, long ingredientId);
    }

    interface IngredientEditView {
        void loadingStarted();
        void loadingFailed(String message);
        void saveSuccessful();
        void saveFailed(String message);
    }
}
