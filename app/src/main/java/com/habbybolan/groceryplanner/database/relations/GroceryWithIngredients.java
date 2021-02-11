package com.habbybolan.groceryplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habbybolan.groceryplanner.database.entities.GroceryEntity;
import com.habbybolan.groceryplanner.database.entities.GroceryIngredientBridge;
import com.habbybolan.groceryplanner.database.entities.IngredientEntity;

import java.util.List;

public class GroceryWithIngredients {

    @Embedded public GroceryEntity grocery;
    @Relation(

            parentColumn = "groceryId",
            entityColumn = "ingredientName",
            associateBy = @Junction(
                    value = GroceryIngredientBridge.class,
                    parentColumn = "groceryId",
                    entityColumn = "ingredientName")
    )
    public List<IngredientEntity> ingredients;
}
