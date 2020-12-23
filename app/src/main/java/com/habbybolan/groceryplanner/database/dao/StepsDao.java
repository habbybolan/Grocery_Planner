package com.habbybolan.groceryplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.habbybolan.groceryplanner.database.entities.StepsEntity;

import java.util.List;

@Dao
public interface StepsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStep(StepsEntity stepsEntity);

    @Delete
    void deleteStep(StepsEntity stepsEntity);

    @Query("SELECT * FROM StepsEntity WHERE recipeId = :recipeId")
    List<StepsEntity> getStepByRecipe(long recipeId);
}
