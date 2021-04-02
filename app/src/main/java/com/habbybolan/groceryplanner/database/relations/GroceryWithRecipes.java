package com.habbybolan.groceryplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryRecipeBridge;
import com.habbybolan.groceryplanner.database.entities.RecipeEntity;

import java.util.List;

public class GroceryWithRecipes {

    @Embedded
    public RecipeEntity recipe;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "groceryId",
            associateBy = @Junction(
                    value = GroceryRecipeBridge.class,
                    parentColumn = "recipeId",
                    entityColumn = "groceryId")
    )
    public List<GroceryEntity> groceries;
}
