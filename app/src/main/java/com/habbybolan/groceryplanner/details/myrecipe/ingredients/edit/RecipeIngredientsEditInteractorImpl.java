package com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsEditInteractorImpl extends RecipeIngredientsInteractorImpl implements RecipeIngredientsContract.InteractorEdit {

    public RecipeIngredientsEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
    }

    @Override
    public void deleteRecipe(MyRecipe myRecipe) {
        databaseAccess.deleteRecipe(myRecipe.getId());
    }

    @Override
    public void deleteIngredient(MyRecipe myRecipe, Ingredient ingredient) {
        databaseAccess.deleteIngredientsFromRecipe(myRecipe.getId(), new ArrayList<Long>(){{add(ingredient.getId());}});
    }

    @Override
    public void deleteIngredients(MyRecipe myRecipe, List<Ingredient> ingredients) {
        List<Long> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) ingredientIds.add(ingredient.getId());
        databaseAccess.deleteIngredientsFromRecipe(myRecipe.getId(), ingredientIds);
    }
}
