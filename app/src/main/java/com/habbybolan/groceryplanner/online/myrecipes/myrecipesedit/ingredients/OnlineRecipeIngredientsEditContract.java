package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.ingredients;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

public interface OnlineRecipeIngredientsEditContract {

    interface IngredientAdapterView {
         void onIngredientClicked(Ingredient ingredient);
    }

    interface IngredientListener {
        OnlineRecipe getRecipe();
    }
}
