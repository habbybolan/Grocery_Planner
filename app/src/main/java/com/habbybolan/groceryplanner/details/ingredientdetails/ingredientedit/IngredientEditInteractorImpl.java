package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;

import java.util.ArrayList;

import javax.inject.Inject;

public class IngredientEditInteractorImpl implements IngredientEditContract.Interactor {

    private DatabaseAccess databaseAccess;

    @Inject
    public IngredientEditInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void deleteIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder != null) {
            if (ingredientHolder.isGrocery())
                databaseAccess.deleteIngredientsFromGrocery(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
            else
                databaseAccess.deleteIngredientsFromRecipe(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});

        } else {
            databaseAccess.deleteIngredient(ingredient.getId());
        }
    }

    @Override
    public void updateIngredient(OfflineIngredientHolder ingredientHolder, String name, String quantity, String quantityType, String foodType, long ingredientId, DbSingleCallbackWithFail<Ingredient> callback) {
        if (Ingredient.isValidName(name)) {
            Ingredient ingredient = setStringsIntoIngredient(name, quantity, quantityType, foodType, ingredientId);
            if (ingredientHolder != null) {
                if (ingredientHolder.isGrocery())
                    databaseAccess.insertUpdateIngredientsIntoGrocery(ingredientHolder.getId(), ingredient, callback);
                else
                    databaseAccess.insertUpdateIngredientsIntoRecipe(ingredientHolder.getId(), ingredient, callback);
            } else {
                databaseAccess.addIngredient(ingredient, callback);
            }
        }
    }

    /**
     * Set the values of the saved Ingredient.
     * @param name          name for the Ingredient
     * @param quantity      quantity for the Ingredient
     * @param quantityType  quantityType for the Ingredient
     */
    private Ingredient setStringsIntoIngredient(String name, String quantity, String quantityType, String foodType, long ingredientId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        if (!quantity.equals("")) ingredient.setQuantity(Float.parseFloat(quantity));
        else ingredient.setQuantity(0);
        if (!quantityType.equals(""))
            ingredient.setQuantityMeasId(MeasurementType.getMeasurementId(quantityType));
        ingredient.setFoodType(foodType);
        ingredient.setId(ingredientId);
        return ingredient;
    }
}
