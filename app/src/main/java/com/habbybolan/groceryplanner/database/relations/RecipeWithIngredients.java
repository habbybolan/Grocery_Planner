package com.habbybolan.groceryplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habbybolan.groceryplanner.database.entities.IngredientEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;
import com.habbybolan.groceryplanner.database.entities.RecipeIngredientBridge;

import java.util.List;

public class RecipeWithIngredients {

    @Embedded
    public RecipeEntity recipe;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "ingredientName",
            associateBy = @Junction(
                    value = RecipeIngredientBridge.class,
                    parentColumn = "recipeId",
                    entityColumn = "ingredientName")
    )
    public List<IngredientEntity> ingredients;
}
