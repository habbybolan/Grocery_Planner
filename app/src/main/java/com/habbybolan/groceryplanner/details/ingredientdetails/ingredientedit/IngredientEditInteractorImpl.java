package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

import java.util.ArrayList;

public class IngredientEditInteractorImpl implements IngredientEditInteractor {

    private DatabaseAccess databaseAccess;
    public IngredientEditInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void updateIngredient(OfflineIngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder != null) {
            if (ingredientHolder.isGrocery())
                databaseAccess.insertIngredientsIntoGrocery(ingredientHolder.getId(), new ArrayList<Ingredient>(){{add(ingredient);}});
            else
                databaseAccess.insertIngredientsIntoRecipe(ingredientHolder.getId(), new ArrayList<Ingredient>(){{add(ingredient);}});
        } else {
            databaseAccess.addIngredient(ingredient);
        }
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
    public boolean isNewIngredient(Ingredient ingredient) {
        return ingredient.getName().equals("");
    }

    @Override
    public void deleteRelationship(OfflineIngredientHolder ingredientHolder, Ingredient ingredient) {
        if (ingredientHolder != null) {
            if (ingredientHolder.isGrocery())
                databaseAccess.deleteIngredientsFromGrocery(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
            else
                databaseAccess.deleteIngredientsFromRecipe(ingredientHolder.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
        }
    }
}
